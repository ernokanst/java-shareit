package ru.practicum.shareit.request;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto request, @RequestHeader("X-Sharer-User-Id") int userId, HttpServletResponse response) {
        return requestService.create(request, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getFromUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        return requestService.getFromUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll() {
        return requestService.getAll();
    }

    @GetMapping("/{id}")
    public ItemRequestDto get(@PathVariable int id) {
        return requestService.get(id);
    }
}
