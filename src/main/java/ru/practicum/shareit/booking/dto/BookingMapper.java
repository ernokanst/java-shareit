package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) return null;
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemMapper.toItemDto(booking.getItem(), new ArrayList<>()),
                userMapper.toUserDto(booking.getBooker()),
                booking.getStatus().toString()
        );
    }

    public Booking toBooking(BookingCreateDto booking) {
        return new Booking(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new Item(booking.getItemId()),
                new User(booking.getBooker()),
                BookingStatus.WAITING
        );
    }
}
