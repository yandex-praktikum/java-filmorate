package ru.yandex.practicum.filmorete.exeptions;

public class ValidationFilmException extends Throwable {

    public ValidationFilmException() {
        super();
    }

    public ValidationFilmException(String message) {
        super(message);
    }
}
