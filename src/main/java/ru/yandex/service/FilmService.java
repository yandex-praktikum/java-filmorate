package ru.yandex.service;

import java.util.Collection;
import java.util.Set;
import ru.yandex.model.Film;

public interface FilmService {
    Film createFilm(Film film);
    Film deleteFilm(Film film);
    Film updateFilm(Film newFilm);
    Collection<Film> allFilms();
    Film addLike(Long id, Long userId);
    Film deleteLike(Long id, Long userId);
    Collection<Film> topFilms(Integer count);
    Film getFilmId(Long id);
}
