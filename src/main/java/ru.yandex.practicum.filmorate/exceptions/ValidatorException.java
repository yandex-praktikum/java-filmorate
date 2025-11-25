package ru.yandex.practicum.filmorate.exceptions;

public class ValidatorException extends IllegalArgumentException {
    public ValidatorException(String message) {
        super(message);
    }
}
