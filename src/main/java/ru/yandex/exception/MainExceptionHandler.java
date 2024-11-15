package ru.yandex.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class MainExceptionHandler {
    @ExceptionHandler
    public Map<String, String> handlerNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage());
        return Map.of("errorCode", "404", "errorMessage",
                "NOT_FOUND", "reason", ex.getMessage());
    }

    @ExceptionHandler
    public Map<String, String> handlerValidationException(ValidationException ex) {
        log.error(ex.getMessage());
        return Map.of("errorCode", "400", "errorMessage",
                "BAD_REQUEST", "reason", ex.getMessage());
    }

    @ExceptionHandler
    public Map<String, String> handlerConstraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage());
        return Map.of("errorCode", "400", "errorMessage",
                "BAD_REQUEST", "reason", ex.getMessage());
    }

    @ExceptionHandler
    public Map<String, String> handlerMethodArgumentNotValidException (MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        return Map.of("errorCode", "400", "errorMessage",
                "BAD_REQUEST", "reason", ex.getMessage());
    }

    @ExceptionHandler
    public Map<String, String> handlerException(Exception ex) {
        log.error(ex.getMessage());
        return Map.of("errorCode", "500", "errorMessage",
                "INTERNAL_SERVER_ERROR", "reason", ex.getMessage());
    }
}
