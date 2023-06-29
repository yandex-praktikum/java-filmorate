package ru.yandex.practicum.filmorete.exeptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationUserException extends Throwable {

    public ValidationUserException(String message) {
        super(message);
        log.debug(message);
    }
}
