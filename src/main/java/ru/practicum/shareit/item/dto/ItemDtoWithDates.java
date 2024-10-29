package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithDates {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private int owner;
    private int request;
    private List<CommentDto> comments;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
}
