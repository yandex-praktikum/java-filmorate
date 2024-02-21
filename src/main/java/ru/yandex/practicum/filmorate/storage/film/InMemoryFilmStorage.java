package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.Constant.FORMATTER;
import static ru.yandex.practicum.filmorate.constant.Constant.REGEX_DATE;


@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final InMemoryUserStorage userStorage;

    private Integer id = 0;

    private Integer createId() {
        return ++id;
    }

    public Film getFilm(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            log.info("Фильма с таким id нет");
            throw new ValidException("Фильма с таким id нет", HttpStatus.NOT_FOUND);
        }
    }


    public Film addLike(int id, int userId) {
        if (films.containsKey(id) && userStorage.doesIdExist(userId)) {
            films.get(id).getLikes().add(userId);
            return films.get(id);
        } else {
            log.info("Пользователя или фильма с таким id нет");
            throw new ValidException("Пользователя или фильма с таким id нет", HttpStatus.NOT_FOUND);
        }
    }

    public Film deleteLike(int id, int userId) {
        if (films.containsKey(id) && userStorage.doesIdExist(userId)) {
            if (films.get(id).getLikes().contains(userId)) {
                films.get(id).getLikes().remove(userId);
                return films.get(id);
            } else {
                log.info("Лайка с таким id нет");
                throw new ValidException("Лайка с таким id нет", HttpStatus.NOT_FOUND);
            }
        } else {
            log.info("Фильма с таким id нет");
            throw new ValidException("Фильма с таким id нет", HttpStatus.NOT_FOUND);
        }
    }

    public List<Film> getTopTenOfFilms(int count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film createFilm(Film film) {
        if (validDate(film.getReleaseDate())) {
            film.setId(createId());
            films.put(film.getId(), film);
            film.setLikes(new HashSet<>());
            log.info("Фильм " + film.getName() + " добавлен");
            return film;

        } else {
            log.info("Невозможно добавить фильм {} из за года выпуска", film);
            throw new ValidException("Невозможно добавить фильм " + film + " из за года выпуска", HttpStatus.BAD_REQUEST);
        }
    }


    public Film updateFilm(Film film) {
        if (validDate(film.getReleaseDate())) {
            if (!films.containsKey(film.getId())) {
                log.info("Фильма с таким id нет");
                throw new ValidException("Фильма с таким id нет", HttpStatus.NOT_FOUND);
            } else {
                if (film.getLikes() == null) {
                    film.setLikes(new HashSet<>());
                }
                films.put(film.getId(), film);
                log.info("Фильм {} обновлён", film.getName());
                return film;
            }
        } else {
            log.info("Невозможно добавить фильм {} из за года выпуска", film);
            throw new ValidException("Невозможно добавить фильм " + film + " из за года выпуска", HttpStatus.BAD_REQUEST);
        }
    }


    public List<Film> getALlFilms() {
        return new ArrayList<>(films.values());
    }

    private boolean validDate(LocalDate localDate) {
        LocalDate date = LocalDate.parse(localDate.format(FORMATTER));
        if (date.isBefore(REGEX_DATE)) {
            return false;
        } else {
            return true;
        }
    }
}
