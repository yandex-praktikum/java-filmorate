package ru.yandex.practicum.filmorate.controllers;

public class ValidationException extends RuntimeException{
    ValidationException(final String message) {
        super(message);
    }
}
