package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ecxeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
//@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film getFilmById(Long id) {
        try {
            return filmStorage.getFilmById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Такого фильма нет в списке!");
        }
    }

    public Film userLikesFilm(Long id, Long userId) {
        return filmStorage.userLikesFilm(id, userId);
    }

    public Film deleteLikesFilm(Long id, Long userId) {
        return filmStorage.deleteLikesFilm(id, userId);
    }

    public Collection<Film> listFirstCountFilm(int count) {
        Collection<Film> films;
        films = sortingToDown().stream()
                .limit(count)
                .toList();
        return films;
    }

    public List<Film> sortingToDown() {
        ArrayList<Film> listFilms = new ArrayList<>(filmStorage.getAllFilms());
        listFilms.sort((Film film1, Film film2) ->
                Integer.compare(film2.getLikes().size(), film1.getLikes().size())
        );
        return listFilms;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }
}
