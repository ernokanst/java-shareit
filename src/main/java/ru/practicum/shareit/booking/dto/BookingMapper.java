package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapper {
    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus().toString()
        );
    }

    public Booking toBooking(BookingDto booking) {
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
