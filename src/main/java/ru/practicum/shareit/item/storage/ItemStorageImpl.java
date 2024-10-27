package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemStorageImpl implements ItemStorage {
    private Map<Integer, Item> items = new LinkedHashMap<>();
    private Map<Integer, Set<Integer>> itemsByUser = new LinkedHashMap<>();
    private int currentId = 1;

    @Override
    public Item add(Item item) {
        item.setId(currentId++);
        int owner = item.getOwner();
        if (!(itemsByUser.containsKey(owner))) {
            itemsByUser.put(owner, new HashSet<>());
        }
        items.put(item.getId(), item);
        itemsByUser.get(owner).add(item.getId());
        return item;
    }

    @Override
    public Item update(Item item) {
        items.replace(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getFromUser(int userId) {
        return itemsByUser.get(userId).stream().map(x -> items.get(x)).toList();
    }

    @Override
    public Optional<Item> get(int id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public void delete(int id) {
        items.remove(id);
    }

    @Override
    public List<Item> search(String text) {
        return items.values().stream()
                .filter(x -> (x.getName().toUpperCase().contains(text.toUpperCase()) ||
                        x.getDescription().toUpperCase().contains(text.toUpperCase())))
                .filter(Item::isAvailable)
                .collect(Collectors.toList());
    }
}
