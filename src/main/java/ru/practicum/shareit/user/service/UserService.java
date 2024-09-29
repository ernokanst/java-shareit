package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto add(UserDto user);

    UserDto update(UserDto user, Integer id);

    List<UserDto> get();

    UserDto getUser(Integer id);

    void deleteUser(Integer id);
}
