package ru.yandex.practicum.filmorate.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;

@Service
@Validated
public class FilmService {
    HashMap<Integer, Film> filmHashMap = new HashMap<>();

    public ResponseEntity<String> addFilms(@Valid Film film) {
        filmHashMap.put(film.getId(), film);
        return ResponseEntity.ok("Film added");
    }

    public HashMap<Integer, Film> getAllFilms() {
        return filmHashMap;
    }

    public ResponseEntity<String> updateFilms(@Valid Film film) {
        Film oldFilm = filmHashMap.get(film.getId());
        if (oldFilm ==null) {
            ResponseEntity.notFound();
            throw new NotFoundException(HttpStatus.NOT_FOUND,"Film id did not find") ;

        } else {
            filmHashMap.remove(oldFilm);
            filmHashMap.put(film.getId(), film);
            return ResponseEntity.ok("Film updeted");
        }
    }

}
