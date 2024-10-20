package ru.practicum.shareit.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.user.model.User;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class EmailExistsException extends RuntimeException {
    public User user;

    public EmailExistsException(String message, User user) {
        super(message);
        this.user = user;
    }
}
