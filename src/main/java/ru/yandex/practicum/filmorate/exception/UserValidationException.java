package ru.yandex.practicum.filmorate.exception;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}