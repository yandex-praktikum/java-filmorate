package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;

import java.util.List;

/**
 * Интерфейс хранилища фильмов Filmorate
 */
public interface FilmStorage {
    List<Film> findAll();

    Film add(Film film);

    Film update(Film film);

    Film findFilmById(Integer id);
}
