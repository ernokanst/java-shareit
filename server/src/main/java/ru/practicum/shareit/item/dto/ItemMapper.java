package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {
    public ItemDto toItemDto(Item item, List<Comment> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                comments.stream().map(this::toCommentDto).toList(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public ItemDtoWithDates toItemDtoWithDates(Item item, int userId, List<Comment> comments, Booking last, Booking next) {
        return new ItemDtoWithDates(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                comments != null ? comments.stream().map(this::toCommentDto).toList() : new ArrayList<>(),
                item.getOwner().getId() == userId ? toBookingDto(last) : null,
                item.getOwner().getId() == userId ? toBookingDto(next) : null,
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public Item toItem(ItemDto item) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                item.getRequestId() != null ? new ItemRequest(item.getRequestId()) : null
        );
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated().toString());
    }

    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) return null;
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                null,
                null,
                booking.getStatus().toString()
        );
    }
}
