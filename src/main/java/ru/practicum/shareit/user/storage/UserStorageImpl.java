package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.EmailExistsException;
import ru.practicum.shareit.user.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserStorageImpl implements UserStorage {
    private Map<Integer, User> users = new LinkedHashMap<>();
    private int currentId = 1;

    @Override
    public User add(User user) {
        if (users.values().stream().map(User::getEmail).collect(Collectors.toSet()).contains(user.getEmail())) {
            throw new EmailExistsException("Пользователь с указанной почтой уже существует");
        }
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user, Integer id) {
        if (users.values().stream().anyMatch(x -> (x.getEmail().equals(user.getEmail()) && x.getId() != user.getId()))) {
            throw new EmailExistsException("Пользователь с указанной почтой уже существует");
        }
        users.replace(id, user);
        return user;
    }

    @Override
    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> get(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> get(String email) {
        return users.values().stream().filter(x -> x.getEmail().equals(email)).findFirst();
    }

    @Override
    public void delete(Integer id) {
        users.remove(id);
    }
}
