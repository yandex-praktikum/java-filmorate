package ru.yandex.practicum.filmorate.exceptions;

public class UserValidationException extends ValidationException{

    public UserValidationException(String parameter) {
        super(parameter);
    }
}
