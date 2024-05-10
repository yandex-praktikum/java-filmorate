package ru.yandex.practicum.filmorate.service.film.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private final Map<Long, Film> films = new HashMap<>();
    private Long currentId = 1L;

    public Film addFilm(Film film) {
        Long id = currentId++;
        film.setId(id);
        films.put(id, film);
        log.info("Added new film with ID: {} and title: {}", id, film.getName());
        return film;
    }

    public Film updateFilm(Long id, Film film) {
        if (films.containsKey(id)) {
            film.setId(id);
            films.put(id, film);
            log.info("Updated film with ID: {}", id);
            return film;
        } else {
            log.warn("Attempted to update non-existent film with ID: {}", id);
            return null;
        }
    }

    public List<Film> getAllFilms() {
        log.debug("Fetching all films.");
        return new ArrayList<>(films.values());
    }
}