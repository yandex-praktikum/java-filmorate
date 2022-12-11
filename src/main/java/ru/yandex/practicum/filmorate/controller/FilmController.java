package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idGlobal = 0;

    private int generateId() {
        idGlobal += 1;
        return idGlobal;
    }

    @GetMapping
    public Collection<Film> findAll(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}'",
                request.getMethod(), request.getRequestURI());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка валидации!");
        }
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: '{}'!", film.getName());
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка валидации!");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Данные фильма '{}' обновлены!", film.getName());
        } else {
            throw new RuntimeException("Нет пользователя с таким id!");
        }
        return film;
    }
}
