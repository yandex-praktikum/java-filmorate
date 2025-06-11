package ru.yandex.practicum.filmorate.exception.exeptions;

public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(String message) {
        super(message);
    }
}
