package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserStorage {
    private Map<Integer, User> users = new LinkedHashMap<>();
    private int currentId = 1;

    public User add(User user) {
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user, Integer id) {
        User newUser = users.get(id);
        if (user.getName() != null) {
            newUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            newUser.setEmail(user.getEmail());
        }
        users.replace(id, newUser);
        return newUser;
    }

    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    public User get(Integer id) {
        return users.get(id);
    }

    public Optional<User> get(String email) {
        return users.values().stream().filter(x -> x.getEmail().equals(email)).findFirst();
    }

    public void delete(Integer id) {
        users.remove(id);
    }

    public Set<String> getEmails() {
        return users.values().stream().map(User::getEmail).collect(Collectors.toSet());
    }
}
