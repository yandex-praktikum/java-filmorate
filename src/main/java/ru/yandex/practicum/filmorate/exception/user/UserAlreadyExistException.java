package ru.yandex.practicum.filmorate.exception.user;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
