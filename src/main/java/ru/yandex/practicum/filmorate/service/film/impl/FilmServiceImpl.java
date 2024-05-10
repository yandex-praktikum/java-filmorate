package ru.yandex.practicum.filmorate.service.film.impl;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FilmServiceImpl implements FilmService {

    private final Map<Long, Film> films = new HashMap<>();
    private Long currentId = 0L;

    public Film addFilm(Film film) {
        Long id = currentId++;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    public Film updateFilm(Long id, Film film) {
        if (films.containsKey(id)) {
            film.setId(id);
            films.put(id, film);
            return film;
        }
        return null;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}