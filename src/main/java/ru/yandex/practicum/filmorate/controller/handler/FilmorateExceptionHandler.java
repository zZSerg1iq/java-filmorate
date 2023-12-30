package ru.yandex.practicum.filmorate.controller.handler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.DataConflictException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalDataException;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class})
public class FilmorateExceptionHandler {

    @ExceptionHandler(InternalDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalUserNotFoundException(final InternalDataException e) {
        return Map.of(
                "Ошибка сервера", "Невозможно выполнить запрос",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserNotFound(final DataNotFoundException e) {
        return Map.of(
                "Ошибка", e.getMessage()
        );
    }

    @ExceptionHandler(DataConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleHappiness(final DataConflictException e) {
        return Map.of("Ошибка", e.getMessage());
    }


}
