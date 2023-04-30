package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {
    private HttpStatus httpStatus;

    public ValidationException(String s, HttpStatus httpStatus) {
        super(s);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ValidationException(String s) {
        super(s);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public static ValidationException createBadRequestException(String message) {
        return new ValidationException(message, HttpStatus.BAD_REQUEST);
    }

    public static ValidationException createNotFoundException(String message) {
        return new ValidationException(message, HttpStatus.NOT_FOUND);
    }


}
