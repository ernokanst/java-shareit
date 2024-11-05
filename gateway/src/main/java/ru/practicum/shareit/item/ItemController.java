package ru.practicum.shareit.item;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid ItemDto item, @RequestHeader("X-Sharer-User-Id") int userId, HttpServletResponse response) {
        return itemClient.add(item, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody ItemDto item, @PathVariable int id, @RequestHeader("X-Sharer-User-Id") int userId, HttpServletResponse response) {
        item.setId(id);
        return itemClient.update(item, userId);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemClient.get(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable int id, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemClient.getItem(id, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable int id) {
        return itemClient.deleteItem(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> comment(@RequestBody CommentDto comment, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemClient.comment(comment, itemId, userId);
    }
}
