package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto item, int userId);

    ItemDto update(ItemDto item, int userId);

    List<ItemDto> get(int userId);

    ItemDto getItem(int id);

    void deleteItem(int id);

    List<ItemDto> search(String query);
}
