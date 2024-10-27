package ru.practicum.shareit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@Getter
public class ValidationException extends RuntimeException {
    private Object failedObject = null;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Object object) {
        super(message);
        failedObject = object;
    }
}
