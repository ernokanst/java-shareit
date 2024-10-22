package ru.practicum.shareit.item;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import java.util.List;
import java.util.NoSuchElementException;

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
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.get(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable int id) {
        return itemService.getItem(id);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundException exception) {
        log.error(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.item);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElement(NoSuchElementException exception) {
        log.error(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Объект не найден");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
