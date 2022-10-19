package ru.yandex.practicum.filmorate.utils;

public class ValidationException extends Exception {
    private final int inputValue;

    public ValidationException(final String message, final int inputValue) {
        super(message);
        this.inputValue = inputValue;
    }


    public String getDetailMessage() {
        return getMessage() + " id = " + inputValue;
    }
}
