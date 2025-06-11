package ru.yandex.practicum.filmorate.exception.exeptions;

public class ValidateLoginIncorrectException extends RuntimeException {
    public ValidateLoginIncorrectException(String message) {
        super(message);
    }
}
