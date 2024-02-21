package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerException {
    @ExceptionHandler(ValidException.class)
    public ResponseEntity<ResponseError> notFound(ValidException e) {
        return new ResponseEntity<>(ResponseError.builder()
                .message(e.getMessage())
                .build(), e.getStatus());
    }
}