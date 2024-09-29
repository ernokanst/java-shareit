package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User add(User user);

    User update(User user, Integer id);

    List<User> get();

    Optional<User> get(Integer id);

    Optional<User> get(String email);

    void delete(Integer id);
}
