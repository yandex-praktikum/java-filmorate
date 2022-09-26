package ru.yandex.practicum.filmorate.exception;

public class InvalidBirthdateException extends Throwable {
    public InvalidBirthdateException() {
        System.out.println("Ошибка InvalidBirthdateException: пользователь из будущего");
    }

    public String getMessage() {
        return "Ошибка InvalidBirthdateException: пользователь из будущего";
    }
}
