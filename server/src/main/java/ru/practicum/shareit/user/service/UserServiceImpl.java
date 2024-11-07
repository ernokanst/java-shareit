package ru.practicum.shareit.user.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailExistsException;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.dto.*;
import ru.practicum.shareit.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto add(UserDto user) {
        User u = userMapper.toUser(user);
        try {
            return userMapper.toUserDto(userRepository.save(u));
        } catch (DataIntegrityViolationException e) {
            throw new EmailExistsException("Пользователь с указанной почтой уже существует", u);
        }
    }

    @Override
    public UserDto update(UserUpdateDto user, int id) {
        User newUser = userRepository.findById(id).orElseThrow();
        if (user.getName() != null) {
            newUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            newUser.setEmail(user.getEmail());
        }
        try {
            return userMapper.toUserDto(userRepository.save(newUser));
        } catch (DataIntegrityViolationException e) {
            throw new EmailExistsException("Пользователь с указанной почтой уже существует", newUser);
        }
    }

    @Override
    public List<UserDto> get() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(int id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow());
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
