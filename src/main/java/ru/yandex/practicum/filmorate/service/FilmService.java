package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {

    private final ArrayList<Film> films = new ArrayList<>();
    private int currentId = 1;

    public Film addFilm(Film film) {
        validateFilm(film);
        film.setId(currentId);
        currentId++;
        films.add(film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    public Film updateFilm(int id, Film updatedFilm) {

        validateFilm(updatedFilm);

        Optional<Film> existFilm = films.
                                    stream().
                                    filter(film -> film.getId() == id).
                                    findFirst();

        if (existFilm.isPresent()) {

            Film film = existFilm.get();
            film.setName(updatedFilm.getName());
            film.setDescription(updatedFilm.getDescription());
            film.setReleaseDate(updatedFilm.getReleaseDate());
            film.setDuration(updatedFilm.getDuration());
            log.info("Фильм обновлен: {}", film);

            return film;

        } else {
            log.warn("Фильм с указанным id {} не был найден", id);
            throw new ValidationException("Фильм с таким id не найден");
        }

    }

    public ArrayList<Film> getAllFilms() {
        return films;
    }

    private void validateFilm(Film film) {

        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Ошибка валидации фильма: название не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым!");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Ошибка валидации фильма: максимальная длина описания — 200 символов.");
            throw new ValidationException("Максимальная длина описания фильма - 200 символов!");
        }

        if (film.getReleaseDate() == null || film.getReleaseDate().before(new Date(1895 - 1900, 11, 28))) {
            log.error("Ошибка валидации фильма: дата релиза не может быть раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28.11.1895!");
        }

        if (film.getDuration() == null || film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.error("Ошибка валидации фильма: продолжительность фильма должна быть положительным числом.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом!");
        }

    }

}
