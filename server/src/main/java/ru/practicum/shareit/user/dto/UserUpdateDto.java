package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    private Integer id;
    private String name;
    private String email;
}
