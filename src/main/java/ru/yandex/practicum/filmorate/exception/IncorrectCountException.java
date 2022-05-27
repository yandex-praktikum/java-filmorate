package ru.yandex.practicum.filmorate.exception;

public class IncorrectCountException extends RuntimeException{
    public IncorrectCountException(String message) {
        super(message);
    }
}
