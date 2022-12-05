package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    List<Film> getAll();

    void deleteFilm(Long id);

    void deleteAll();

    Optional<Film> getById(Long id);

    void addLike(Long idUser, Long idFilm);

    void removeLike(Long idUser, Long idFilm);

    List<Film> getOrderRate(Integer count);
}
