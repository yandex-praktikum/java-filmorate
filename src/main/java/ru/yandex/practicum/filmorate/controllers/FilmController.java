package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
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
    final LocalDate BIRTH_MOVIE=LocalDate.of(1895,12,28);
    HashMap<Integer,Film> films=new HashMap<>();
    @GetMapping
    public List<Film> gettingAllFilms()  {
        log.debug("Получен запрос GET /films.");
        return new ArrayList<>(films.values());
    }
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if(isValid(film)) {
            films.put(film.getId(), film);
            log.debug("Получен запрос POST. Передан обьект {}",film);
            return film;
        }else{
            log.error("Передан запрос POST с некорректным данными фльима.");
            throw new ValidationException("Ошибка валидации фильма!");
        }
    }
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (isValid(film) && films.containsKey(film.getId())) {
            if (!films.containsValue(film)) {
                films.replace(film.getId(), film);
            } else {
                log.debug("Фильм уже загружен.");
            }
            return film;
        } else {
            log.error("Передан запрос POST с некорректным данными фильма.");
            throw new ValidationException("Ошибка валидации фильма!");
        }

    }

    private boolean isValid(Film film){
        return  film.getReleaseDate().isAfter(BIRTH_MOVIE);
    }

}
