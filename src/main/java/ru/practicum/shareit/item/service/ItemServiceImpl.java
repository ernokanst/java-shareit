package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto add(ItemDto item, int userId) {
        Item i = itemMapper.toItem(item);
        i.setOwner(userRepository.findById(userId).orElseThrow().getId());
        return itemMapper.toItemDto(itemRepository.save(i), commentRepository.findByItemId(i.getId()));
    }

    @Override
    public ItemDto update(ItemDto item, int userId) {
        Item newItem = itemRepository.findById(item.getId()).orElseThrow();
        if (newItem.getOwner() != userId) {
            throw new NotFoundException("Пользователь не соответствует владельцу вещи", item);
        }
        if (item.getName() != null) {
            newItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }
        return itemMapper.toItemDto(itemRepository.save(newItem), commentRepository.findByItemId(newItem.getId()));
    }

    @Override
    public List<ItemDtoWithDates> get(int userId) {
        return itemRepository.findByOwner(userId).stream()
                .map(x -> itemMapper.toItemDtoWithDates(x, userId, commentRepository.findByItemId(x.getId()),
                        bookingRepository.findByItem_IdAndEndIsBeforeOrderByEndDesc(x.getId(), LocalDateTime.now()),
                        bookingRepository.findByItem_IdAndStartIsAfterOrderByStartAsc(x.getId(), LocalDateTime.now())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDtoWithDates getItem(int id, int userId) {
        Item i = itemRepository.findById(id).orElseThrow();
        return itemMapper.toItemDtoWithDates(i, userId, commentRepository.findByItemId(i.getId()),
                bookingRepository.findByItem_IdAndEndIsBeforeOrderByEndDesc(i.getId(), LocalDateTime.now()),
                bookingRepository.findByItem_IdAndStartIsAfterOrderByStartAsc(i.getId(), LocalDateTime.now()));
    }

    @Override
    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text).stream()
                .map(x -> itemMapper.toItemDto(x, commentRepository.findByItemId(x.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto comment(Comment comment) {
        comment.setItem(itemRepository.findById(comment.getItem().getId()).orElseThrow());
        comment.setAuthor(userRepository.findById(comment.getAuthor().getId()).orElseThrow());
        if (bookingRepository.findByBooker_IdAndItem_IdIsAndStatusIsAndEndIsBefore(comment.getAuthor().getId(),
                comment.getItem().getId(), BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("Пользователь не брал вещь в аренду");
        }
        return itemMapper.toCommentDto(commentRepository.save(comment));
    }
}
