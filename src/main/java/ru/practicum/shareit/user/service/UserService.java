package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import java.util.List;

public interface UserService {
    User add(User user);

    User update(User user, Integer id);

    List<User> get();

    User getUser(Integer id);

    void deleteUser(Integer id);
}
