package ru.yandex.practicum.filmorate.controller;

public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }
}
