package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryFilmStorage  implements FilmStorage {

    private Integer globalId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(Integer id) {
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        film.setId(globalId);
        films.put(globalId, film);
        globalId++;

        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);

        return film;
    }
}
