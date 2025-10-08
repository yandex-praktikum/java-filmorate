package ru.yandex.practicum.filmorate.ecxeption;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
