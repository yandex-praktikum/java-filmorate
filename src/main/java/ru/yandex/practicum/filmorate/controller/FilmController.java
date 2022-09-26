package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private long filmId = 0;
    private final LocalDate oldDate = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> findAll() {
        log.info("Запрос на получение списка фильмов");
        List <Film> list = new ArrayList<>();
        for(Film f : films.values()) {
            list.add(f);
        }
        return list;
    }

    public Map<Long, Film> findAllForTest() {
        return films;
    }

    @SneakyThrows
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        if(films.containsKey(film.getId())) {
            log.info("Попытка добавить фильм с уже существующим id");
            throw new IdAlreadyExistException();
        }
        if(film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.info("Попытка добавить фильм без названия");
            throw new InvalidNameException();
        }
        if(film.getDescription().length() > 200) {
            log.info("Попытка добавить фильм с описанием более 200 символов");
            throw new InvalidDescriptionException();
        }
        if(film.getReleaseDate().isBefore(oldDate)) {
            log.info("Попытка добавить фильм с датой релиза ранее 1895-12-28");
            throw new InvalidReleaseDateException();
        }
        if(film.getDuration() <= 0) {
            log.info("Попытка добавить фильм продолжительностью <= 0");
            throw new InvalidDurationException();
        }
        for(Film f : films.values()) {
            if (f.getName().equals(film.getName())) {
                log.info("Попытка добавить уже существующий фильм");
                throw new IdAlreadyExistException();
            }
        }

        filmId++;
        film.setId(filmId);
        log.info("Установлен id фильма: {}", filmId);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @SneakyThrows
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if(film.getId() == null || !films.containsKey(film.getId())) {
            log.info("Попытка обновить фильм с несуществующим или пустым id: {}", film.getId());
            throw new InvalidIdException();
        }
        if(film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.info("Попытка обновить фильм без указания названия, id фильма: {}", film.getId());
            throw new InvalidNameException();
        }
        if(film.getDescription().length() > 200) {
            log.info("Попытка добавить фильм с описанием более 200 символов");
            throw new InvalidDescriptionException();
        }
        if(film.getReleaseDate().isBefore(oldDate)) {
            log.info("Попытка обновить фильм с датой релиза ранее 1895-12-28, id фильма: {}", film.getId());
            throw new InvalidReleaseDateException();
        }
        if(film.getDuration() <= 0) {
            log.info("Попытка обновить фильм с продолжительностью <= 0, id фильма: {}", film.getId());
            throw new InvalidDurationException();
        }

        films.put(film.getId(), film);
        log.info("Фильм обновлен: {}", film);

        return film;
    }
}
