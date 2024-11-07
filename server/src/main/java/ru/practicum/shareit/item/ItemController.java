package ru.practicum.shareit.item;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    private final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody ItemDto item, @RequestHeader(USER_ID_HEADER) int userId, HttpServletResponse response) {
        return new ResponseEntity<>(itemService.add(item, userId), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody ItemDto item, @PathVariable int id, @RequestHeader(USER_ID_HEADER) int userId, HttpServletResponse response) {
        item.setId(id);
        return new ResponseEntity<>(itemService.update(item, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader(USER_ID_HEADER) int userId) {
        return new ResponseEntity<>(itemService.get(userId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable int id, @RequestHeader(USER_ID_HEADER) int userId) {
        return new ResponseEntity<>(itemService.getItem(id, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        return new ResponseEntity<>(itemService.search(text), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> comment(@RequestBody CommentDto comment, @PathVariable int itemId, @RequestHeader(USER_ID_HEADER) int userId) {
        return new ResponseEntity<>(itemService.comment(comment, itemId, userId, LocalDateTime.now()), HttpStatus.OK);
    }
}
