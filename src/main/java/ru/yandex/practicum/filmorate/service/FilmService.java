package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.ActionHasAlreadyDoneException;
import ru.yandex.practicum.filmorate.exeptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    Map<Integer, Integer> likes = new HashMap<>(); //ключ - ID фильма,
    // значения - ID пользователей, кто лайкнул
    List<Film> popularFilms = new ArrayList<>();

    public Film findFilm(int id) { // получение пользователя по ID
        try {
            if (!inMemoryFilmStorage.getFilms().containsKey(id)) {
                throw new ObjectNotFoundException("There no film with such id");
            }
        } catch (ObjectNotFoundException exception) {
            throw new ObjectNotFoundException(exception.getMessage());
        }
        return inMemoryFilmStorage.getFilms().get(id);
    }

    public void setLike(int id, int userId) { //добавление лайка
        try {
            if (!findFilm(id).getLikes().isEmpty() && findFilm(id).getLikes().contains((long) userId)) {
                throw new ActionHasAlreadyDoneException("This user has already liked this film.");
            } else {
                findFilm(id).setLikes(userId);
            }
        } catch (ActionHasAlreadyDoneException exception) {
            throw new ActionHasAlreadyDoneException(exception.getMessage());
        }
    }

    public void deleteLike(int id, int userId) { //удаление лайка
        try {
            if (userId < 0) {
                throw new ObjectNotFoundException("Wrong id.");
            }
            findFilm(id).deleteLike(userId);
        } catch (ObjectNotFoundException exception) {
            throw new ObjectNotFoundException(exception.getMessage());
        }
    }

    public List<Film> getPopularFilms(int count) {
        popularFilms.clear();
        for (Film film : inMemoryFilmStorage.findAll()) {
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


