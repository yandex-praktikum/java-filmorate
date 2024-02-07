package ru.yandex.practicum.filmorate.exception.user;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}