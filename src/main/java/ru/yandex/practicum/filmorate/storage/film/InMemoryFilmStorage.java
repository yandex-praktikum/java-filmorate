package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static ru.yandex.practicum.filmorate.validation.Validation.validateFilm;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 1;
    private final List<Film> films;

    public InMemoryFilmStorage(List<Film> films) {
        this.films = films;
    }

    @Override
    public Film createFilm(Film film){
        log.info("Получен POST-запрос с объектом Film: {}", film);
        film.setId(filmId);
        validateFilm(film);
        films.add(film);
        log.info("Фильм {} c id={} добавлен, объект: {}", film.getName(), filmId, film);
        increaseFilmId();
        return film;
    }

    @Override
    public Film updateFilm(Film film){
        log.info("Получен PUT-запрос с объектом Film: {}", film);
        validateFilm(film);
        int filmId = film.getId();
        for (Film u : films) {
            if (u.getId() == filmId) {
                films.set(films.indexOf(u), film);
                log.info("Фильм c id={} обновлён", filmId);
                return film;
            }
        }
        throw new IdNotFoundException("Фильм с id=" + filmId + " не найден");
    }

    @Override
    public String deleteFilm(int id) {
        for (Film u : films) {
            if (u.getId() == id){
                films.remove(u);
                log.info("Фильм c id={} удалён", id);
                return String.format("Фильм c id=%s удалён", id);
            }
        }
        throw new IdNotFoundException("Фильм с id=" + id + " не найден");
    }

    @Override
    public Film findFilm(int id){
        for (Film f : films){
            if (f.getId() == id){
                return f;
            }
        }
        throw new IdNotFoundException("Фильм с id=" + id + " не найден");
    }

    @Override
    public List<Film> getFilms(){
        return films;
    }

    private void increaseFilmId(){
        filmId++;
    }
}
