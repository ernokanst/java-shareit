package ru.practicum.shareit.user;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid UserDto user, HttpServletResponse response) {
        return userClient.add(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody @Valid UserUpdateDto user, @PathVariable int id, HttpServletResponse response) {
        return userClient.update(user, id);
    }

    @GetMapping
    public ResponseEntity<Object> get() {
        return userClient.get();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable int id) {
        return userClient.getUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        return userClient.deleteUser(id);
    }
}
