package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> add(@RequestBody @Valid BookingCreateDto booking, @RequestHeader("X-Sharer-User-Id") int userId) {
		return bookingClient.add(booking, userId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approve(@PathVariable int bookingId, @RequestParam boolean approved, @RequestHeader("X-Sharer-User-Id") int userId) {
		return bookingClient.approve(userId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> get(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
		return bookingClient.get(bookingId, userId);
	}

	@GetMapping
	public ResponseEntity<Object> getAll(@RequestParam(value = "state", defaultValue = "ALL") BookingState state, @RequestHeader("X-Sharer-User-Id") int userId) {
		return bookingClient.getAll(state, userId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllOwner(@RequestParam(value = "state", defaultValue = "ALL") BookingState state, @RequestHeader("X-Sharer-User-Id") int userId) {
		return bookingClient.getAllOwner(state, userId);
	}
}
