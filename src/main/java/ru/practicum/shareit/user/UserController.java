package ru.practicum.shareit.user;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.EmailExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto add(@RequestBody @Valid UserDto user, HttpServletResponse response) {
        try {
            return userService.add(user);
        } catch (EmailExistsException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            log.error(e.getMessage());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto user, @PathVariable Integer id, HttpServletResponse response) {
        try {
            return userService.update(user, id);
        } catch (EmailExistsException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            log.error(e.getMessage());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping
    public List<UserDto> get() {
        try {
            return userService.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        try {
            return userService.getUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
