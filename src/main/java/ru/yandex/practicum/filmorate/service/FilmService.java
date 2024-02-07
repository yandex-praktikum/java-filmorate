package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilmService {

    private final Map<Integer, Film> films = new HashMap<>();
    private static int index = 0;

    public Film addFilm(Film film) {
        film.setId(++index);
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (film != null && films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            return film;
        } else {
            throw new FilmNotFoundException("Ошибка обновления фильма!");
        }
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
