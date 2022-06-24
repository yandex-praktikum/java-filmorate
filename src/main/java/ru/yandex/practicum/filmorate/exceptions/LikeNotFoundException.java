package ru.yandex.practicum.filmorate.exceptions;

public class LikeNotFoundException extends NotFoundException{
    public LikeNotFoundException() {
        super("Like");
    }
}
