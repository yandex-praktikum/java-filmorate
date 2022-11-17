package ru.yandex.practicum.filmorate.controller.validation;

public class ValidationException extends Exception {
    public ValidationException() {
    }

    public ValidationException(final String message) {
        super(message);
    }
}
