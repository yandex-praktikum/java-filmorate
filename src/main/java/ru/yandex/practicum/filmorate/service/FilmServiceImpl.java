package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@Service
@Validated
public class FilmServiceImpl implements FilmService {
    public HashMap<Integer, Film> filmHashMap = new HashMap<>();
    private static int id = 1;

    @Override
    public @Valid Film updateFilm(@Valid Film film) {
        Film filmToUpdate = filmHashMap.get(film.getId());
        if (filmToUpdate == null) {
            log.error("Film don't find");
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Film don't find");
        } else {
            filmHashMap.remove(filmToUpdate);
            filmHashMap.put(film.getId(), film);
            return film;
        }
    }

    @Override
    public @Valid Film addFilms(@Valid Film film) {
        if (film.getId() == 0) {
            film.setId(id++);
        }
        filmHashMap.put(film.getId(), film);
        return film;
    }

    @Override
    public HashMap<Integer, Film> getAllFilms() {
        return filmHashMap;
    }


}
