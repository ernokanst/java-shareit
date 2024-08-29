package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        checkUser(user);
        return userStorage.add(user);
    }

    public User update(User user, Integer id) {
        if (userStorage.get(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        checkRepeatingEmail(user);
        return userStorage.update(user, id);
    }

    public List<User> get() {
        return userStorage.get();
    }

    public User getUser(Integer id) {
        if (userStorage.get(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.get(id);
    }

    public void deleteUser(Integer id) {
        userStorage.delete(id);
    }

    public void checkUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Почта не указана");
        }
        if (!(user.getEmail().contains("@")) || user.getEmail().contains(" ")) {
            throw new ValidationException("Почта указана неверно");
        }
        checkRepeatingEmail(user);
    }

    public void checkRepeatingEmail(User user) {
        if (userStorage.getEmails().contains(user.getEmail())) {
            throw new EmailExistsException("Пользователь с указанной почтой уже существует");
        }
    }
}
