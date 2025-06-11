package ru.yandex.practicum.filmorate.exception.exeptions;

public class ReleaseDateValidationException extends RuntimeException {
    public ReleaseDateValidationException(String message) {
        super(message);
    }
}
