package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate LOW_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private Long newId = 1L;

    private Long getNewId() {
        return newId++;
    }

    @Override
    public Optional<Film> createFilm(Film film) {

        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidateException("пустое наименование фильма");
        }
        if (film.getDescription().isBlank() || film.getDescription() == null) {
            throw new ValidateException("пустое описание");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidateException("размер описания превышает 200 символов");
        }
        if (film.getReleaseDate().isBefore(LOW_RELEASE_DATE)) {
            throw new ValidateException("дата релиза неверна");
        }
        if (film.getDuration() <= 0) {
            throw new ValidateException("длительность фильма должна быть положительной");
        }

        film.setId(getNewId());
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (film.getId() <= 0) {
            throw new NotFoundException("id должен быть > 0");
        }
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public List<Film> getAll() {
        List<Film> list = new ArrayList<>();
        for (Long id : films.keySet()) {
            list.add(films.get(id));
        }
        return list;
    }

    @Override
    public void deleteFilm(Long id) {
        if (films.get(id) == null) {
            throw new NotFoundException("film not found");
        }
        films.remove(id);
    }

    @Override
    public void deleteAll() {
        films.clear();
    }

    @Override
    public Optional<Film> getById(Long id) {
        if (films.get(id) == null) {
            throw new NotFoundException("film not found");
        }
        return Optional.of(films.get(id));
    }

    @Override
    public void addLike(Long idUser, Long idFilm) {
        films.get(idFilm).addLike(idUser);
        films.get(idFilm).setRate(films.get(idFilm).getRate() + 1);
    }

    @Override
    public void removeLike(Long idUser, Long idFilm) {
        films.get(idFilm).removeLike(idUser);
        films.get(idFilm).setRate(films.get(idFilm).getRate() - 1);
    }

    @Override
    public List<Film> getOrderRate(Integer count) {
        if (count == null || count < 1) {
            throw new ValidateException("Количество фильмов должно быть > 0");
        }
        List<Film> filmRate = new ArrayList<>();
        if (films.size() < count) {
            count = films.size();
        }
        while (filmRate.size() != count) {
            Map<Long, Film> films1 = new HashMap<>(films);
            Long max = 0L;
            Long id = 0L;
            for (Film f: films1.values()) {
                if (f.getRate() > max) {
                    max = f.getRate();
                    id = f.getId();
                }
            }
            filmRate.add(films1.get(id));
            films1.remove(id);
        }
        return filmRate;
    }
}
