package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
@AllArgsConstructor
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer owner;
    private Integer request;
}
