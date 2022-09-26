package ru.yandex.practicum.filmorate.exception;

public class InvalidNameException extends Throwable {
    public InvalidNameException() {
        System.out.println("Ошибка InvalidNameException: отсутствует имя");
    }

    public String getMessage() {
        return "Ошибка InvalidNameException: отсутствует имя";
    }
}
