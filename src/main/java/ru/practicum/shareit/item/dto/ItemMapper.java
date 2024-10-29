package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {
    public ItemDto toItemDto(Item item, List<Comment> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner().getId(),
                item.getRequest(),
                comments.stream().map(this::toCommentDto).toList()
        );
    }

    public ItemDtoWithDates toItemDtoWithDates(Item item, int userId, List<Comment> comments, BookingDto last, BookingDto next) {
        return new ItemDtoWithDates(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner().getId(),
                item.getRequest(),
                comments.stream().map(this::toCommentDto).toList(),
                item.getOwner().getId() == userId ? last : null,
                item.getOwner().getId() == userId ? next : null
        );
    }

    public Item toItem(ItemDto item) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                new User(item.getOwner()),
                item.getRequest()
        );
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                toItemDto(comment.getItem(), new ArrayList<>()),
                comment.getCreated().toString());
    }
}
