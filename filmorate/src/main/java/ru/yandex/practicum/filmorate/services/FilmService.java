package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {

        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Integer userId, Integer filmId) {
        if (userStorage.getUserById(userId) != null && filmStorage.getFilmById(filmId) != null) {
            filmStorage.getFilms().get(filmId).getRatedUsers().add(userId);
            userStorage.getUsers().get(userId).getFavoriteFilms().add(filmId);
        } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с id \"%s\" или фильма с id \"%s\" не существует."
                            , userId, filmId));
        }
    }

    public void removeLike(Integer userId, Integer filmId) {
        if (userStorage.getUserById(userId) != null && filmStorage.getFilmById(filmId) != null) {
            filmStorage.getFilms().get(filmId).getRatedUsers().remove(userId);
            userStorage.getUsers().get(userId).getFavoriteFilms().remove(filmId);
        } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с id \"%s\" или фильма с id \"%s\" не существует."
                            , userId, filmId));
        }
    }

    public List<Film> getListPopularFilms(int limit) {
        List<Film> topFilms = filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(f -> f.getRatedUsers().size()))
                .collect(Collectors.toList());
        //Вопрос к ревьюеру: Есть ли возможность отразить коллекцию прямо в стриме?
        Collections.reverse(topFilms);
        topFilms = topFilms.stream()
                .limit(limit)
                .collect(Collectors.toList());
        System.out.println("Im here");
        return topFilms;
    }
}
