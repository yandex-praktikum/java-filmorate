package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception handleValidationException(final Exception e){
        return new Exception(String.format(e.getMessage()));
    }

    @ExceptionHandler(FilmUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Exception handleNotFoundException (final FilmUserNotFoundException e){
        return new Exception(String.format(e.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception handleThrowable(final MethodArgumentNotValidException e){
        return new Exception("Некоректные данные " + e.getStackTrace());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Exception handleThrowable(final Throwable e){
        return new Exception("Непредвиденное исключение " + e.getStackTrace());
    }
}
