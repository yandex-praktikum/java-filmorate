package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.FilmNotFoundException;
import ru.yandex.practicum.exception.UserNotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.ErrorResponse;

/**
 * Класс обработчик исключений и ошибок
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFoundException(final UserNotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse filmNotFoundException(final FilmNotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final ValidationException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRuntimeEx(final RuntimeException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}