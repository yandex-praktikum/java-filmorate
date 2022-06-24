package ru.yandex.practicum.filmorate.exceptions;

public class FilmValidationException extends ValidationException {

    public FilmValidationException(String parameter) {
        super(parameter);
    }
}
