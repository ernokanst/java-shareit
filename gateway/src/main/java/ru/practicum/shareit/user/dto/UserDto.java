package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank(message = "Имя не указано")
    private String name;
    @NotBlank(message = "Почта не указана")
    @Email(message = "Почта указана неверно")
    private String email;
}
