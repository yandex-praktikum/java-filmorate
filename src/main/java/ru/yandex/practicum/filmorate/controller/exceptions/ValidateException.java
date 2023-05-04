package ru.yandex.practicum.filmorate.controller.exceptions;

public class ValidateException extends RuntimeException {
    private final String message;

    public ValidateException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
