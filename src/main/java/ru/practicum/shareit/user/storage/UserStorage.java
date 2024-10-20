package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User add(User user);

    User update(User user, int id);

    List<User> get();

    Optional<User> get(int id);

    Optional<User> get(String email);

    void delete(int id);
}
