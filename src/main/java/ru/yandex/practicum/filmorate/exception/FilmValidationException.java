package ru.yandex.practicum.filmorate.exception;

public class FilmValidationException extends RuntimeException {
    public FilmValidationException(String message) {
        super(message);
    }
}