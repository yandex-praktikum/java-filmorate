package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ecxeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film getFilmById(Long id) {
        if (filmStorage.getFilmById(id) == null) {
            throw new NotFoundException("Такого фильма нет в списке!");
        }
        return filmStorage.getFilmById(id);
    }

    public Film userLikesFilm(Long id, Long userId) {
        Film film = filmStorage.getFilmById(id);
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Такого юзера нет в списке!");
        }
        if (film == null) {
            throw new NotFoundException("Такого фильма нет в списке!");
        }
        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLikesFilm(Long id, Long userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Такого юзера нет!");
        }
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new NotFoundException("Такого фильма нет в списке!");
        }
        film.getLikes().remove(userId);
        return film;
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
