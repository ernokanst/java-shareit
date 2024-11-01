package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import java.util.List;

public interface UserService {
    UserDto add(UserDto user);

    UserDto update(UserUpdateDto user, int id);

    List<UserDto> get();

    UserDto getUser(int id);

    void deleteUser(int id);
}
