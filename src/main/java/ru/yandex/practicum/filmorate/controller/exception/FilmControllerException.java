package ru.yandex.practicum.filmorate.controller.exception;

public class FilmControllerException extends ControllerException {
    public static final String FILM_ALREADY_EXISTS;
    public static final String FILM_NOT_FOUND;

    static {
        FILM_ALREADY_EXISTS = "Фильм уже добавлен ранее >%s";
        FILM_NOT_FOUND = "Фильм не найден >%s";
    }

    public FilmControllerException() {
    }

    public FilmControllerException(String message) {
        super(message);
    }
}