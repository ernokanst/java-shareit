package ru.practicum.shareit.item;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto add(@RequestBody @Valid ItemDto item, @RequestHeader("X-Sharer-User-Id") int userId, HttpServletResponse response) {
        return itemService.add(item, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto item, @PathVariable int id, @RequestHeader("X-Sharer-User-Id") int userId, HttpServletResponse response) {
        item.setId(id);
        return itemService.update(item, userId);
    }

    @GetMapping
    public List<ItemDtoWithDates> get(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.get(userId);
    }

    @GetMapping("/{id}")
    public ItemDtoWithDates getItem(@PathVariable int id, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItem(id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto comment(@RequestBody CommentDto comment, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.comment(comment, itemId, userId, LocalDateTime.now());
    }
}
