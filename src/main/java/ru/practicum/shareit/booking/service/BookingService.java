package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import java.util.List;

public interface BookingService {
    Booking add(BookingDto booking, int userId);

    Booking approve(int userId, int bookingId, boolean approved);

    Booking get(int bookingId, int userId);

    List<Booking> getAll(BookingState state, int userId);

    List<Booking> getAllOwner(BookingState state, int userId);
}
