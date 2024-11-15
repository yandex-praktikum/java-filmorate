package ru.yandex.storage;

import ru.yandex.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film createFilm(Film film);

    Collection<Film> allFilms();

    Film updateFilm(Film newFilm);

    Film deleteFilm(Film film);
}
