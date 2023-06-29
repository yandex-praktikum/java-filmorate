package ru.yandex.practicum.filmorete.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorete.exeptions.ValidationFilmException;
import ru.yandex.practicum.filmorete.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private final Set<String> names = new HashSet<>();
    private Integer lastIdentification = 1;

    @GetMapping()
    public Collection<Film> findAll() {
        log.debug("Запрос всех фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping()
    public Film create(@RequestBody Film film) throws ValidationFilmException {
        if (names.contains(film.getName())) {
            throw new ValidationFilmException("Фильм уже есть в коллекции!");
        }
        else {
            validatorFilms(film);
            film.setId(getLastIdentification());
            films.put(film.getId(), film);
            names.add(film.getName());
            log.debug("Добавление нового фильма: {}", film.getName());
            return film;
        }
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) throws ValidationFilmException {
        if (film.getId() == null || film.getId() < 1) {
            throw new ValidationFilmException("Не указан ID фильма!");
        }
        if (!films.containsKey(film.getId())) {
            throw new ValidationFilmException("Фильм не найден для обновления!");
        }

        validatorFilms(film);

        Film oldFilm = films.get(film.getId());
        names.remove(oldFilm.getName());
        names.add(film.getName());

        films.put(film.getId(), film);
        log.debug("Обновление фильма: {}", film.getName());
        return film;
    }

    @DeleteMapping()
    public void clear() {
        films.clear();
        names.clear();
        lastIdentification = 1;
    }

    private void validatorFilms(Film film) throws ValidationFilmException {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationFilmException("Имя фильма равно null или отсутствует!");
        }
        if (film.getDescription() == null) {
            throw new ValidationFilmException("Описание фильма равно null или отсутствует!");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationFilmException("Длинна описания превышает 200 символов!");
        }
        if (film.getReleaseDate() == null) {
            throw new ValidationFilmException("Дата релиза фильма равно null или отсутствует!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationFilmException("Дата релиза не должна быть раньше 28 декабря 1895 года!");
        }
        if (film.getDuration() == null) {
            throw new ValidationFilmException("Продолжительность фильма равно null или отсутствует!");
        }
        if (film.getDuration() < 1) {
            throw new ValidationFilmException("Продолжительность фильма должна быть положительной!");
        }
    }

    private Integer getLastIdentification() {
        return lastIdentification++;
    }
}