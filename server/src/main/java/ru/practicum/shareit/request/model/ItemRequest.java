package ru.practicum.shareit.request.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ItemRequest implements Serializable {
    private int id;
    private String description;
    private int requester;
    private LocalDateTime created;

    public ItemRequest(int id) {
        this.id = id;
    }
}
