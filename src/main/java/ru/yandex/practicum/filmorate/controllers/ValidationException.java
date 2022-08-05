package ru.yandex.practicum.filmorate.controllers;

public class ValidationException extends RuntimeException{
    public ValidationException(final String message) {
        super(message);
    }
}
