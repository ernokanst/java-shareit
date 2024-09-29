package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto item, Integer userId);

    ItemDto update(ItemDto item, Integer userId);

    List<ItemDto> get(Integer userId);

    ItemDto getItem(Integer id);

    void deleteItem(Integer id);

    List<ItemDto> search(String query);
}
