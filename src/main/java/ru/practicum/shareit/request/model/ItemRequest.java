package ru.practicum.shareit.request.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private int id;
    private String description;
    private int requester;
    private LocalDateTime created;

    public ItemRequest(int id) {
        this.id = id;
    }
}
