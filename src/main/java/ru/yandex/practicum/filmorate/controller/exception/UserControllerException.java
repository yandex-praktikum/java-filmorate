package ru.yandex.practicum.filmorate.controller.exception;

public class UserControllerException extends ControllerException {
    public static final String USER_ALREADY_EXISTS;
    public static final String USER_NOT_FOUND;

    static {
        USER_ALREADY_EXISTS = "Пользователь уже добавлен ранее >%s";
        USER_NOT_FOUND = "Пользователь не найден >%s";
    }

    public UserControllerException() {
    }

    public UserControllerException(String message) {
        super(message);
    }
}