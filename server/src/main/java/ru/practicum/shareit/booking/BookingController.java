package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.service.BookingService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    private final String userIdHeader = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody BookingCreateDto booking, @RequestHeader(userIdHeader) int userId) {
        return new ResponseEntity<>(bookingService.add(booking, userId), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable int bookingId, @RequestParam boolean approved, @RequestHeader(userIdHeader) int userId) {
        return new ResponseEntity<>(bookingService.approve(userId, bookingId, approved), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@PathVariable int bookingId, @RequestHeader(userIdHeader) int userId) {
        return new ResponseEntity<>(bookingService.get(bookingId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(value = "state", defaultValue = "ALL") BookingState state, @RequestHeader(userIdHeader) int userId) {
        return new ResponseEntity<>(bookingService.getAll(state, userId), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwner(@RequestParam(value = "state", defaultValue = "ALL") BookingState state, @RequestHeader(userIdHeader) int userId) {
        return new ResponseEntity<>(bookingService.getAllOwner(state, userId), HttpStatus.OK);
    }
}
