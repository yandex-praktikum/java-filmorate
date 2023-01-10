package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.chrono.ChronoLocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.storage.film.DbFilmStorage.LOW_RELEASE_DATE;

@Service
public class FilmDBService implements FilmService {
    private static final ChronoLocalDate LOW_RELEASE_DATE = ;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmDBService(@Qualifier("dbFilmStorage") FilmStorage filmStorage
            , @Qualifier("dbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @Override
    public Optional<Film> createFilm(Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidateException("пустое наменование фильма");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidateException("размер описания превышает 200 символов");
        }

        if (film.getDescription().isBlank() || film.getDescription() == null) {
            throw new ValidateException("пустое описание");
        }

        if (film.getReleaseDate().isBefore(LOW_RELEASE_DATE)) {
            throw new ValidateException("дата релиза неверна");
        }
        if (film.getDuration() <= 0) {
            throw new ValidateException("длительность фильма должна быть положительной");
        }
        if (film.getMpa() == null) {
            throw new ValidateException("отсутствует MPA");
        }



        return filmStorage.createFilm(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public void deleteFilm(Long id) {
        filmStorage.deleteFilm(id);
    }

    @Override
    public void deleteAll() {
        filmStorage.deleteAll();
    }

    @Override
    public Optional<Film> getById(Long id) {
        return filmStorage.getById(id);
    }

    @Override
    public void addLike(Long idFilm, Long idUser) {
        if (filmStorage.getById(idFilm) != null && userStorage.getById(idUser) != null
                && idFilm > 0 && idUser > 0) {
            filmStorage.addLike(idUser, idFilm);
        } else {
            throw new NotFoundException("user id=" + idUser + " or film id=" + idFilm + " не найдены");
        }

    }

    @Override
    public void removeLike(Long idFilm, Long idUser) {
        if (filmStorage.getById(idFilm) != null && userStorage.getById(idUser) != null) {
            filmStorage.removeLike(idUser, idFilm);
        } else {
            throw new ValidateException("user id=" + idUser + " or film id=" + idFilm + " не найдены");
        }

    }

    @Override
    public List<Optional<Film>> getMaxRating(Integer countRate) {
        Integer count = countRate;
        if (count == 0 || count == null) {
            count = 10;
        }
        List<Optional<Film>> listOrder = filmStorage.getOrderRate(count);
        return listOrder;
    }

    @Override
    public Mpa getMpa(Long idMpa) {
        return filmStorage.getMpa(idMpa);
    }

    @Override
    public TreeSet<Genre> getGenres(Film film) {
        return filmStorage.getGenres(film);
    }
}
