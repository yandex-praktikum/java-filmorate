package ru.yandex.practicum.filmorate.exception;

public class InvalidReleaseDateException extends Throwable {
    public InvalidReleaseDateException() {
        System.out.println("Ошибка InvalidReleaseDateException: фильм слишком старый");
    }

    public String getMessage() {
        return "Ошибка InvalidReleaseDateException: фильм слишком старый";
    }

}
