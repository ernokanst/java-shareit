package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "is_available", nullable = false)
    private boolean available;
    @Column(name = "owner_id", nullable = false)
    private Integer owner;
    @Column(name = "request_id")
    private Integer request;

    public Item(Integer id) {
        this.id = id;
    }
}
