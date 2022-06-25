package ru.yandex.practicum.filmorate.exceptions;

public class UserNotFoundException extends NotFoundException{

    public UserNotFoundException() {
        super("User");
    }
}
