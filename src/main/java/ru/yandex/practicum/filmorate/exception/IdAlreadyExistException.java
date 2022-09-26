package ru.yandex.practicum.filmorate.exception;

public class IdAlreadyExistException extends Throwable {
    public IdAlreadyExistException() {
        System.out.println("Ошибка IdAlreadyExistException: такой id уже существует");
    }

    public String getMessage() {
        return "Ошибка IdAlreadyExistException: такой id уже существует";
    }
}
