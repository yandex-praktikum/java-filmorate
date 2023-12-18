package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ApiError;

import java.time.LocalDateTime;

@RestControllerAdvice("ru.yandex.practicum")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequest(final BadRequestException e) {
        log.warn(e.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Body parameters are not valid.", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFound(final NotFoundException e) {
        log.warn(e.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, "Entity not found.", e.getMessage(), LocalDateTime.now()));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
