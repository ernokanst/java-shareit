package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.*;
import java.util.List;

public interface BookingService {
    BookingDto add(BookingCreateDto booking, int userId);

    BookingDto approve(int userId, int bookingId, boolean approved);

    BookingDto get(int bookingId, int userId);

    List<BookingDto> getAll(BookingState state, int userId);

    List<BookingDto> getAllOwner(BookingState state, int userId);
}
