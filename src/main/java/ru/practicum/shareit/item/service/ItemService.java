package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemService {
    Item add(Item item, Integer userId);

    Item update(Item item, Integer id, Integer userId);

    List<Item> get(Integer userId);

    Item getItem(Integer id);

    void deleteItem(Integer id);

    List<Item> search(String query);
}
