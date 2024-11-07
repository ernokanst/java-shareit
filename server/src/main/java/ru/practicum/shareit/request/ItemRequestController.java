package ru.practicum.shareit.request;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    private final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ItemRequestDto request, @RequestHeader(USER_ID_HEADER) int userId, HttpServletResponse response) {
        return new ResponseEntity<>(requestService.create(request, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getFromUser(@RequestHeader(USER_ID_HEADER) int userId) {
        return new ResponseEntity<>(requestService.getFromUser(userId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(requestService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        return new ResponseEntity<>(requestService.get(id), HttpStatus.OK);
    }
}
