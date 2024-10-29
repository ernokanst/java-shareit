package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    Integer id;
    String text;
    String authorName;
    ItemDto item;
    String created;
}
