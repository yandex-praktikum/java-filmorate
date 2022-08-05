package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    final LocalDate BIRTH_MOVIE = LocalDate.of(1895, 12, 28);
    HashMap<Integer, Film> films = new HashMap<>();
    private Integer id=0;

    @GetMapping
    public List<Film> gettingAllFilms() {
        log.debug("Получен запрос GET /films.");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        createId(film);
        isValid(film);
        films.put(film.getId(), film);
        log.debug("Получен запрос POST. Передан обьект {}", film);
        return film;

    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        isValid(film);
        if (films.containsKey(film.getId())) {
            if (!films.containsValue(film)) {
                films.replace(film.getId(), film);
                log.debug("Фильм обновлен");
            } else {
                log.debug("Фильм уже загружен.");
            }
            return film;
        } else {
            log.error("Передан запрос PUT с некорректным данными фльима {}.", film);
            throw new ValidationException(HttpStatus.resolve(500));
        }

    }

    private void isValid(Film film) {
        if (film.getId() < 1 || film.getName().isBlank() || film.getDescription().length() > 200 ||
                film.getDescription().isBlank() || film.getReleaseDate().isBefore(BIRTH_MOVIE) || film.getDuration() < 0) {
            log.error("Передан запрос POST с некорректным данными фльима {}.", film);
            throw new ValidationException(HttpStatus.resolve(500));

        }
    }
    private void createId(Film film){
        id++;
        film.setId(id);
    }

}
