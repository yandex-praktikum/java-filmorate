package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final int MAX_DESCRIPTION_SIZE = 200;
    private static final LocalDate FIRST_EVER_FILM = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films;
    private static long idCounter = 0;

    @Autowired
    public InMemoryFilmStorage() {
        films = new HashMap<>();
    }

    private static long createID() {
        return ++idCounter;
    }

    @Override
    public Film addFilm(Film film) {
        if (films.values().stream()
                .filter(x -> x.getName().equalsIgnoreCase(film.getName()))
                .anyMatch(x -> x.getReleaseDate().equals(film.getReleaseDate()))) {
            log.error(
                    "Фильм '{}' с датой релиза '{}' уже добавлен.",
                    film.getName(),
                    film.getReleaseDate()
            );
            throw new ValidationException("This film already exists");
        } else if (isValid(film)) {
            film.setId(createID());
            films.put(film.getId(), film);
            log.info("Добавлен фильм: {}", film.getName());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Фильм '{}' с id '{}' не найден в списке!", film.getName(), film.getId());
            throw new FilmNotFoundException("The film not found in the map!");
        }
        if (isValid(film)) {
            films.put(film.getId(), film);
            log.info("Отредактирован фильм '{}'", film.getName());
        }
        return film;
    }

    @Override
    public Map<Long, Film> getAllFilms() {
        return films;
    }

    @Override
    public Film getFilm(Long id) {
        if (films.get(id) == null) {
            log.error("Фильм с id '{}' не найден в списке!", id);
            throw new FilmNotFoundException(String.format("Фильм с id '%d' не найден.", id));
        }
        return films.get(id);
    }

    private boolean isValid(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("invalid film name");
        } else if (film.getDescription().length() > MAX_DESCRIPTION_SIZE || film.getDescription().isBlank()) {
            log.error(String.format("Максимальная длина описания — %s символов", MAX_DESCRIPTION_SIZE));
            throw new ValidationException("invalid description");
        } else if (film.getReleaseDate().isBefore(FIRST_EVER_FILM)) {
            log.error("Дата релиза не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("invalid release date");
        } else if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("invalid duration");
        } else {
            return true;
        }
    }
}
