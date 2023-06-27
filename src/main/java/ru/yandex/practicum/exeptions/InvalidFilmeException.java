package ru.yandex.practicum.exeptions;

public class InvalidFilmeException extends Throwable {

    public InvalidFilmeException(String message) {
        super(message);
    }
}
