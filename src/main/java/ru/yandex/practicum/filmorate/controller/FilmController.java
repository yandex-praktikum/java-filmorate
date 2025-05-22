package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/film")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private long nextInt = 1;
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);


    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Запрос списка всех фильмов. Текущее количество: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.debug("Попытка добавить фильм: {}", film);
        try {
            validateFilmFields(film);
            validateFilmUnique(film);

            film.setId(genNextId());
            films.put(film.getId(), film);

            log.info("Фильм добавлен успешно. ID: {}, Название: {}", film.getId(), film.getName());
            return film;
        } catch (ConditionsNotMetException | DuplicatedDataException e) {
            log.warn("Ошибка при добавлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Попытка внести обновления: {}", film);
        log.debug("Входящие данные для обновления: name={}, date={}",
                film.getName(), film.getDate());
        try {
            if (film.getId() == null) {
                throw new ConditionsNotMetException("Id должен быть указан");
            }

            Film existingFilm = films.get(film.getId());
            if (existingFilm == null) {
                throw new ConditionsNotMetException("Фильм не найден");
            }

            validateFilmFieldsUpdate(film, existingFilm);
            updateValidFields(film, existingFilm);

            log.info("Фильм обновлён. ID: {}, Новое название: {}", existingFilm.getId(), existingFilm.getName());
            return existingFilm;

        } catch (ConditionsNotMetException | DuplicatedDataException e) {
            log.warn("Ошибка при обновлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    private void validateFilmFields(Film film) {
        log.debug("Начало валидации {}", film);
        if (film.getName() == null || film.getName().trim().isBlank()) {
            log.error("Название фильма не указано");
            throw new ConditionsNotMetException("Name не может быть пустым");
        }
        if (film.getDescription() == null) {
            log.error("Описание фильма не указано");
            throw new ConditionsNotMetException("Описание должно быть указано");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("Описание фильма слишком длинное");
            throw new ConditionsNotMetException("Описание не может превышать" + MAX_DESCRIPTION_LENGTH + " " +
                    "символов");
        }
        if (film.getDate() == null) {
            log.error("Дата релиза не указана");
            throw new ConditionsNotMetException("Дата релиза должна быть указана");
        }
        if (film.getDate().isBefore(CINEMA_BIRTHDAY)) {
            log.error("Некорректная дата релиза");
            throw new ConditionsNotMetException("Дата релиза не может быть раньше " + CINEMA_BIRTHDAY);
        }
        if (film.getDuration() == null) {
            log.error("Продолжительность не указана");
            throw new ConditionsNotMetException("Продолжительность должна быть указана");
        }
        if (film.getDuration() <= 0) {
            log.error("Некорректная продолжительность");
            throw new ConditionsNotMetException("Продолжительность должна быть положительным числом");
        }
        log.debug("Валидация завершена {}", film);
    }

    private void validateFilmUnique(Film film) {
        films.values().stream()
                .filter(f -> f.getName().equalsIgnoreCase(film.getName()))
                .findFirst()
                .ifPresent(f -> {
                    throw new DuplicatedDataException("Такое название фильма уже используется");
                });
    }

    private void validateFilmFieldsUpdate(Film film, Film existingFilm) {
        if (film.getName() != null && !film.getName().equals(existingFilm.getName())) {
            films.values().stream()
                    .filter(f -> !f.getId().equals(existingFilm.getId()))
                    .filter(f -> f.getName().equalsIgnoreCase(existingFilm.getName()))
                    .findFirst()
                    .ifPresent(f -> {
                        throw new DuplicatedDataException("Фильм с таким названием уже существует");
                    });
        }
        if (film.getName() == null || film.getName().trim().isBlank()) {
            throw new ConditionsNotMetException("Name не может быть пустым");
        }

        if (film.getDescription() != null) {
            if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
                throw new ConditionsNotMetException("Описание не может превышать " + MAX_DESCRIPTION_LENGTH + " символов");
            }
        }

        if (film.getDate() != null) {
            if (film.getDate().isBefore(CINEMA_BIRTHDAY)) {
                throw new ConditionsNotMetException("Дата релиза не может быть раньше " + CINEMA_BIRTHDAY);
            }
        }

        if (film.getDuration() != null) {
            if (film.getDuration() <= 0) {
                throw new ConditionsNotMetException("Продолжительность должна быть положительным числом");
            }
        }
    }

    private void updateValidFields(Film film, Film existingFilm) {
        if (film.getName() != null) {
            existingFilm.setName(film.getName());
        }
        if (film.getDate() != null) {
            existingFilm.setDate(film.getDate());
        }
        if (film.getDescription() != null) {
            existingFilm.setDescription(film.getDescription());
        }
        if (film.getDuration() != null) {
            existingFilm.setDuration(film.getDuration());
        }
    }

    private long genNextId() {
        return nextInt++;
    }

}
