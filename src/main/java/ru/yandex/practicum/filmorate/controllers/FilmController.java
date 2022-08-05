package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    final LocalDate BIRTH_MOVIE=LocalDate.of(1895,12,28);
    ArrayList<Film> films=new ArrayList<>();
    @GetMapping
    public List<Film> gettingAllFilms() {
        log.debug("Получен запрос GET /films.");
        return films;
    }
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if(isValid(film)) {
            films.add(film);
            log.debug("Получен запрос POST. Передан обьект {}",film);
            return film;
        }else{
            log.error("Передан запрос POST с некорректным данными.");
            throw new ValidationException("Ошибка валидации фильма!");
        }
    }
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if(isValid(film)) {
            for(Film film1:films){
                if(film1.getId()== film.getId()){
                    films.set(films.indexOf(film1),film);
                    log.debug("Получен запрос PUT. Передан обьект {}.",film);
                    return film;
                }else{
                    log.error("Передан запрос PUT с некорректным id.");
                    throw new ValidationException("Отсутствует фильм с данным id.");
                }
            }
        }else{
            log.error("Передан запрос PUT с некорректным данными.");
            throw new ValidationException("Ошибка валидации фильма!");
        }
        return film;
    }

    private boolean isValid(Film film){
        return  film.getReleaseDate().isAfter(BIRTH_MOVIE) && !film.getDuration().isZero();
    }

}
