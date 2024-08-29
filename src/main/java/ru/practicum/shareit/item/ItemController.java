package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item add(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.add(item, userId);
    }

    @PatchMapping("/{id}")
    public Item update(@RequestBody Item item, @PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.update(item, id, userId);
    }

    @GetMapping
    public List<Item> get(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.get(userId);
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable Integer id) {
        return itemService.getItem(id);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
