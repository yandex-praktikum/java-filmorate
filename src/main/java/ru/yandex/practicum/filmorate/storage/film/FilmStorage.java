package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film getFilm(int id);

    List<Film> getTopTenOfFilms(int count);

    Film addLike(int id, int userId);

    Film deleteLike(int id, int userId);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getALlFilms();
}
