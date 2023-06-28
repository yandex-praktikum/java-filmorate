package ru.yandex.practicum.filmorete.exeptions;

public class ValidationUserException extends Throwable {

    public ValidationUserException() {
    }

    public ValidationUserException(String message) {
        super(message);
    }
}
