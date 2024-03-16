package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
        setNewFilmId(film);
        putFilm(film);
        log.debug("Created film {}", film);
        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId()))
            throw new ValidationException("Film with id " + film.getId() + " not exists");
        putFilm(film);
        log.debug("Updated film {}", film);
        return film;
    }

    public void putFilm(Film film) {
        checkCorrect(film);
        films.put(film.getId(), film);
    }

    private void setNewFilmId(Film film) {
        int id = 0;
        if (!films.isEmpty())
            id = Collections.max(films.keySet());
        log.debug("Set film id {}", id);
        film.setId(id + 1);
    }

    private void checkCorrect(Film film) {
        StringBuilder sb = new StringBuilder();

        if (film.getName().isBlank())
            sb.append("Name shouldn't be blank.");
        if (film.getDescription().length() > 200)
            sb.append("Description should be less than 200 symbols.");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            sb.append("Release date should be from 28 december 1895.");
        if (film.getDuration() <= 0)
            sb.append("Duration should be positive value.");

        if (sb.length() > 0) {
            log.debug(sb.toString());
            throw new ValidationException(sb.toString());
        }

    }
}
