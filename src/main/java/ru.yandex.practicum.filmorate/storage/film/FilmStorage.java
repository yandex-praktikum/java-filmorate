package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Optional<Film> findById(long id);

    Collection<Film> findAll();

    void deleteById(long id);

    Film addLike(long filmId, long userId);

    Film removeLike(long filmId, long userId);

    Collection<Film> findPopular(int count);
}
