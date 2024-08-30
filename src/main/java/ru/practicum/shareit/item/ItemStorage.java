package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemStorage {
    private Map<Integer, Item> items = new LinkedHashMap<>();
    private int currentId = 1;

    public Item add(Item item) {
        item.setId(currentId++);
        items.put(item.getId(), item);
        return item;
    }

    public Item update(Item item, Integer id) {
        Item newItem = items.get(id);
        if (item.getName() != null) {
            newItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }
        items.replace(id, newItem);
        return newItem;
    }

    public List<Item> getFromUser(Integer userId) {
        return items.values().stream().filter(x -> Objects.equals(x.getOwner().getId(), userId)).collect(Collectors.toList());
    }

    public Item get(Integer id) {
        return items.get(id);
    }

    public void delete(Integer id) {
        items.remove(id);
    }

    public List<Item> search(String query) {
        return items.values().stream()
                .filter(x -> (x.getName().toUpperCase().contains(query.toUpperCase()) || x.getDescription().toUpperCase().contains(query.toUpperCase())))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
