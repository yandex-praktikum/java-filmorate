package ru.yandex.practicum.filmorate.controller.exception;

public class ControllerException extends Exception {
    public ControllerException() {
    }

    public ControllerException(String message) {
        super(message);
    }
}