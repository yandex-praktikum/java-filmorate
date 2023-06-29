package ru.yandex.practicum.filmorete.exeptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationFilmException extends Throwable {

    public ValidationFilmException(String message) {
        super(message);
        log.debug(message);
    }
}
