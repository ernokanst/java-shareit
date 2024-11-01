package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    private Integer id;
    private String name;
    @Email(message = "Почта указана неверно")
    private String email;
}
