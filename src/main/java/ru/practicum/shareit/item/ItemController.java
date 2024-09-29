package ru.practicum.shareit.item;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
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
    public ItemDto add(@RequestBody @Valid ItemDto item, @RequestHeader("X-Sharer-User-Id") Integer userId, HttpServletResponse response) {
        try {
            return itemService.add(item, userId);
        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            log.error(e.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto item, @PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId, HttpServletResponse response) {
        try {
            item.setId(id);
            return itemService.update(item, userId);
        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            log.error(e.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        try {
            return itemService.get(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable Integer id) {
        try {
            return itemService.getItem(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Integer id) {
        try {
            itemService.deleteItem(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        try {
            return itemService.search(text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
