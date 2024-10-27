package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto item, int userId);

    ItemDto update(ItemDto item, int userId);

    List<ItemDtoWithDates> get(int userId);

    ItemDtoWithDates getItem(int id, int userId);

    void deleteItem(int id);

    List<ItemDto> search(String query);

    CommentDto comment(Comment comment);
}
