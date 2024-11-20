package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import java.util.List;
import java.util.Map;

public interface BookingService {
    BookingDto add(BookingCreateDto booking, int userId);

    BookingDto approve(int userId, int bookingId, boolean approved);

    BookingDto get(int bookingId, int userId);

    List<BookingDto> getAll(BookingState state, int userId);

    List<BookingDto> getAllOwner(BookingState state, int userId);

    Map<Integer, List<Booking>> findLastAndNext(int userId);
}
