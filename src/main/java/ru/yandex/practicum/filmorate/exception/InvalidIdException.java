package ru.yandex.practicum.filmorate.exception;

public class InvalidIdException extends Throwable {
    public InvalidIdException() {
        System.out.println("Ошибка InvalidIdException: некорректный или пустой id");
    }

    public String getMessage() {
        return "Ошибка InvalidIdException: некорректный или пустой id";
    }
}
