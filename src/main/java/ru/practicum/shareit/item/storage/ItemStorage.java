package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item add(Item item);

    Item update(Item item);

    List<Item> getFromUser(Integer userId);

    Optional<Item> get(Integer id);

    void delete(Integer id);

    List<Item> search(String query);
}
