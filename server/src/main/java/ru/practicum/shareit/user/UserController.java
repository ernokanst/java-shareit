package ru.practicum.shareit.user;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody UserDto user, HttpServletResponse response) {
        return new ResponseEntity<>(userService.add(user), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody UserUpdateDto user, @PathVariable int id, HttpServletResponse response) {
        return new ResponseEntity<>(userService.update(user, id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> get() {
        return new ResponseEntity<>(userService.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable int id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
