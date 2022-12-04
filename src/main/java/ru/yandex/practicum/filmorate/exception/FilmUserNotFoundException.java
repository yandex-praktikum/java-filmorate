package ru.yandex.practicum.filmorate.exception;

public class FilmUserNotFoundException extends RuntimeException {

    public FilmUserNotFoundException(String message) {
        super(message);
    }
}