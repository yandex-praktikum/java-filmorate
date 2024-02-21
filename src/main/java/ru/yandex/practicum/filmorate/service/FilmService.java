package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;


@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public Film addLike(int id, int userId) {
        return filmStorage.addLike(id, userId);
    }

    public Film deleteLike(int id, int userId) {
        return filmStorage.deleteLike(id, userId);
    }

    public List<Film> getTopTenOfFilms(String count) {
        int count1 = Integer.parseInt(count);
        return filmStorage.getTopTenOfFilms(count1);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }


    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }


    public List<Film> getALlFilms() {
        return filmStorage.getALlFilms();
    }
}
