package ru.yandex.practicum.filmorate.exceptions;

public class ConditionsNotMetException extends RuntimeException {
    public ConditionsNotMetException(String message) {
        super(message);
    }
}
