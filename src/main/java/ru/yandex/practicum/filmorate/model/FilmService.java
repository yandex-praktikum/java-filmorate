package ru.yandex.practicum.filmorate.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    public Film add(Film film) {
        if (film.getId() == 0) {
            film.setId(currentId);
        }
        films.put(film.getId(), film);
        currentId++;
        return film;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public boolean isAlreadyExists(Film film) {
        boolean flag = false;
        for (Film item : films.values()) {
            if (film.getId() == item.getId()) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        }
        return film;
    }
}
