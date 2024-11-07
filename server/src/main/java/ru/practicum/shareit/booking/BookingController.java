package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.service.BookingService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto add(@RequestBody BookingCreateDto booking, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.add(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable int bookingId, @RequestParam boolean approved, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAll(@RequestParam(value = "state", defaultValue = "ALL") BookingState state, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.getAll(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwner(@RequestParam(value = "state", defaultValue = "ALL") BookingState state, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.getAllOwner(state, userId);
    }
}
