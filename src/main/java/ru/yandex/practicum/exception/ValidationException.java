package ru.yandex.practicum.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
