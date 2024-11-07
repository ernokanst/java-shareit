package ru.practicum.shareit.item.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentDto> comments;
    private Integer requestId;
}
