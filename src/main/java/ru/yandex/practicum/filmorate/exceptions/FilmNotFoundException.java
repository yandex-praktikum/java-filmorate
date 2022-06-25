package ru.yandex.practicum.filmorate.exceptions;

public class FilmNotFoundException extends NotFoundException{
    public FilmNotFoundException() {
        super("Film");
    }
}
