package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addFilm(Film film);

    Film updateFilm(Long id, Film film);

    List<Film> getAllFilms();

}
