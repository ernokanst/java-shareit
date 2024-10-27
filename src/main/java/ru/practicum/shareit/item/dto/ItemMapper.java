package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

@Component
public class ItemMapper {
    public ItemDto toItemDto(Item item, List<Comment> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest(),
                comments.stream().map(this::toCommentDto).toList()
        );
    }

    public ItemDtoWithDates toItemDtoWithDates(Item item, List<Comment> comments, List<Booking> last, List<Booking> next) {
        return new ItemDtoWithDates(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest(),
                comments.stream().map(this::toCommentDto).toList(),
                !(last.isEmpty()) ? last.getFirst().getEnd().toString() : null,
                !(next.isEmpty()) ? next.getFirst().getStart().toString() : null
        );
    }

    public Item toItem(ItemDto item) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getItem(), comment.getCreated().toString());
    }
}
