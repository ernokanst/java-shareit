package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemStorage itemStorage;
    private UserStorage userStorage;
    private ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage, ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto add(ItemDto item, Integer userId) {
        Item i = itemMapper.toItem(item);
        if (userStorage.get(userId).isEmpty()) {
            throw new NotFoundException("Владелец вещи не найден");
        }
        i.setOwner(userStorage.get(userId).get());
        return itemMapper.toItemDto(itemStorage.add(i));
    }

    @Override
    public ItemDto update(ItemDto item, Integer userId) {
        if (itemStorage.get(item.getId()).isEmpty()) {
            throw new NotFoundException("Вещь не найдена");
        }
        Item newItem = itemStorage.get(item.getId()).get();
        if (!Objects.equals(newItem.getOwner().getId(), userId)) {
            throw new NotFoundException("Пользователь не соответствует владельцу вещи");
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
        return itemMapper.toItemDto(itemStorage.update(newItem));
    }

    @Override
    public List<ItemDto> get(Integer userId) {
        return itemStorage.getFromUser(userId).stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto getItem(Integer id) {
        if (itemStorage.get(id).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return itemMapper.toItemDto(itemStorage.get(id).get());
    }

    @Override
    public void deleteItem(Integer id) {
        itemStorage.delete(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.search(text).stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }
}
