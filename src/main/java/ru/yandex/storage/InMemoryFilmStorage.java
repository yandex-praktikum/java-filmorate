package ru.yandex.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.exception.NotFoundException;
import ru.yandex.exception.ValidationException;
import ru.yandex.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Data
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        log.info("Получен запрос на добавление фильма {}", film);
        validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен");
        return film;

    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("Получен запрос на обновление фильма {}", newFilm);
        validate(newFilm);
        if (newFilm.getId() == null) {
            log.error("Ошибка валидации");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.info("фильм успешно обновился");
            return newFilm;
        }
        log.error("Ошибка валидации");
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }


    @Override
    public Collection<Film> allFilms() {
        log.info("Получен запрос на получение фильмом");
        return films.values();
    }

    @Override
    public Film deleteFilm(Film film) {
        if (films.containsKey(film.getId())) {
            return films.remove(film.getId());
        }
        log.error("Ошибка валидации");
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка валидации name");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                log.error("Ошибка валидации description");
                throw new ValidationException("Максимальная длина описания - 200 символов");
            }
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 2, 28))) {
            log.error("Ошибка валидации releaseDate");
            throw new ValidationException("Дата релиза должна быть не раньше 1895.02.28");
        }
        if (film.getDuration() == null || film.getDuration().isNegative()) {
            log.error("Ошибка валидации duration");
            throw new ValidationException("Продолжительность фильма доджна быть положительным числом");
        }
    }
}
