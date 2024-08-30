package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto add(@RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return ItemMapper.toItemDto(itemService.add(ItemMapper.toItem(item), userId));
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto item, @PathVariable Integer id, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return ItemMapper.toItemDto(itemService.update(ItemMapper.toItem(item), id, userId));
    }

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.get(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable Integer id) {
        return ItemMapper.toItemDto(itemService.getItem(id));
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
