package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ActionHasAlreadyDoneException;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    Map<Integer, Integer> likes = new HashMap<>(); //ключ - ID фильма,
    // значения - ID пользователей, кто лайкнул
    List<Film> popularFilms = new ArrayList<>();

    public Film findFilm(int id) { // получение пользователя по ID
            if (!filmStorage.getFilms().containsKey(id)) {
                throw new ObjectNotFoundException("There no film with such id");
            }
        return filmStorage.getFilms().get(id);
    }

    public void setLike(int id, int userId) { //добавление лайка
            if (!findFilm(id).getLikes().isEmpty() && findFilm(id).getLikes().contains((long) userId)) {
                throw new ActionHasAlreadyDoneException("This user has already liked this film.");
            } else {
                findFilm(id).setLikes(userId);
            }
    }

    public void deleteLike(int id, int userId) { //удаление лайка
            if (userId < 0) {
                throw new ObjectNotFoundException("Wrong id.");
            }
            findFilm(id).deleteLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        popularFilms.clear();
        for (Film film : filmStorage.findAll()) {
            likes.put(film.getId(), film.getLikes().size());
        }

        likes = likes.entrySet()
                .stream().sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(count)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        for (Integer id : likes.keySet()) {
            if (popularFilms.isEmpty() || !popularFilms.contains(findFilm(id))) {
                popularFilms.add(findFilm(id));
            }
        }
        return popularFilms;
    }
}


