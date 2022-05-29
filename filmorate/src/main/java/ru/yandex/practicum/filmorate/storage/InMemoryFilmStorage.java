package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Getter
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public List<Film> getFilms() {

        return new ArrayList<>(films.values());
    }
    @Override
    public Film getFilmById(int id){
        Film film;
        if(films.containsKey(id)){
            film = films.get(id);
        } else {
            throw new ObjectNotFoundException(
                    String.format("Фильма с id \"%s\"не существует.", id));
        }
        return film;
    }

    @Override
    public Film create(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма должно быть не более 200 символов");
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getId() == 0) {
            assignId(film);
        } else {
            throw new ValidationException("Фильм должен быть передан без id");
        }
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == 0) {
            throw new ValidationException("Введите id фильма, которого необходимо обновить");
        }
        if(!films.containsKey(film.getId())){
            throw new ObjectNotFoundException("Указанного фильма не существует");
        }
        films.put(film.getId(), film);
        return film;
    }

    private void assignId(Film film) {
        film.setId(id);
        id++;
    }

}
