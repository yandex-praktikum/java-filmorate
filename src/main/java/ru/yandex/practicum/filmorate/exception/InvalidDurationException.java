package ru.yandex.practicum.filmorate.exception;

public class InvalidDurationException extends Throwable {
    public InvalidDurationException() {
        System.out.println("Ошибка InvalidDurationException: продолжительность фильма должна быть положительной");
    }

    public String getMessage() {
        return "Ошибка InvalidDurationException: продолжительность фильма должна быть положительной";
    }
}
