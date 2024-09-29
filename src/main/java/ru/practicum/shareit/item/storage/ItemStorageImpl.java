package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemStorageImpl implements ItemStorage {
    private Map<Integer, Map<Integer, Item>> items = new LinkedHashMap<>();
    private Map<Integer, Integer> itemsByUser = new LinkedHashMap<>();
    private int currentId = 1;

    @Override
    public Item add(Item item) {
        item.setId(currentId++);
        Integer owner = item.getOwner().getId();
        if (!(items.containsKey(owner))) {
            items.put(owner, new HashMap<>());
        }
        items.get(owner).put(item.getId(), item);
        itemsByUser.put(item.getId(), owner);
        return item;
    }

    @Override
    public Item update(Item item) {
        Integer owner = item.getOwner().getId();
        items.get(owner).replace(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getFromUser(Integer userId) {
        return items.get(userId).values().stream().toList();
    }

    @Override
    public Optional<Item> get(Integer id) {
        return Optional.ofNullable(items.get(itemsByUser.get(id)).get(id));
    }

    @Override
    public void delete(Integer id) {
        items.remove(id);
    }

    @Override
    public List<Item> search(String text) {
        Set<Item> allItems = new HashSet<>();
        for (Collection<Item> c : items.values().stream().map(Map::values).collect(Collectors.toSet())) {
            allItems.addAll(c);
        }
        return allItems.stream()
                .filter(x -> (x.getName().toUpperCase().contains(text.toUpperCase()) || x.getDescription().toUpperCase().contains(text.toUpperCase())))
                .filter(Item::isAvailable)
                .collect(Collectors.toList());
    }
}
