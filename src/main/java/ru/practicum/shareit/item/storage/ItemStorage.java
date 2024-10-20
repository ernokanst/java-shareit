package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item add(Item item);

    Item update(Item item);

    List<Item> getFromUser(int userId);

    Optional<Item> get(int id);

    void delete(int id);

    List<Item> search(String query);
}
