package ru.yandex.practicum.filmorete.exeptions;

public class InvalidFilmeException extends Throwable {

    public InvalidFilmeException() {
        super();
    }

    public InvalidFilmeException(String message) {
        super(message);
    }
}
