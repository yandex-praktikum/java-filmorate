package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException {

    public ValidationException() {
    }

    public ValidationException(final String message) {
        super(message);
    }

    public String getDetailMessage() {
        return super.getMessage();
    }

}