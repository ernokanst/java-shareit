package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.dto.*;
import ru.practicum.shareit.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserStorage userStorage;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto add(UserDto user) {
        User u = userMapper.toUser(user);
        return userMapper.toUserDto(userStorage.add(u));
    }

    @Override
    public UserDto update(UserDto user, int id) {
        User newUser = userStorage.get(id).orElseThrow();
        if (user.getName() != null) {
            newUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            newUser.setEmail(user.getEmail());
        }
        return userMapper.toUserDto(userStorage.update(newUser, id));
    }

    @Override
    public List<UserDto> get() {
        return userStorage.get().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(int id) {
        return userMapper.toUserDto(userStorage.get(id).orElseThrow());
    }

    @Override
    public void deleteUser(int id) {
        userStorage.delete(id);
    }
}
