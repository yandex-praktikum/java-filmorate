package ru.yandex.practicum.filmorate.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

public interface FilmStorage {
    final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);
    public List<Film> getFilms();

    public Film getFilmById(int id);

    public Film create(Film film);

    public Film update (Film film);
}
