package ru.practicum.shareit.request;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    private final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemRequestDto request, @RequestHeader(USER_ID_HEADER) int userId, HttpServletResponse response) {
        return requestClient.create(request, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getFromUser(@RequestHeader(USER_ID_HEADER) int userId) {
        return requestClient.getFromUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return requestClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        return requestClient.get(id);
    }
}
