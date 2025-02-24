package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate RELEASE_DATE_CHECK = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATE_CHECK)) {
            String error = "Дата релиза не должна быть ранее 28.12.1895";
            log.error("Ошибка при создании фильма: {}", error);
            throw new ConditionsNotMetException(error);
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} успешно создан", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        try {
            if (film.getId() == 0) {
                throw new ConditionsNotMetException("ID не может быть равен 0");
            }
            long id = film.getId();
            if (film.getReleaseDate().isBefore(RELEASE_DATE_CHECK)) {
                String error = "Дата релиза не должна быть ранее 28.12.1895";
                log.error("Ошибка при создании фильма: {}", error);
                throw new ConditionsNotMetException(error);
            }
            if (films.containsKey(id)) {
                film.setId(id);
                films.put(id, film);
                log.info("Фильм {} успешно обновлён.", film);
            } else {
                throw new NotFoundException("Фильм с ID: " + film.getId() + "не найден");
            }
        } catch (ConditionsNotMetException e){
            log.error("Ошибка при обновлении фильма {}", e.getMessage());
            throw e;
        } catch (NotFoundException e){
            log.error("Ошибка {}", e.getMessage());
            throw e;
        }
         return film;
    }


    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
