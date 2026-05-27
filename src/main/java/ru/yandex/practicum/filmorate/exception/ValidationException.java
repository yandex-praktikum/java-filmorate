package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException {

    // Конструктор класса ValidationException
    public ValidationException(String message) {
        super(message);
    }
}
