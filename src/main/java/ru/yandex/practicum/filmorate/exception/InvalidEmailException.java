package ru.yandex.practicum.filmorate.exception;

public class InvalidEmailException extends Throwable {
    public InvalidEmailException() {
        System.out.println("Ошибка InvalidEmailException: некорректный email");
    }

    public String getMessage() {
        return "Ошибка InvalidEmailException: некорректный email";
    }
}
