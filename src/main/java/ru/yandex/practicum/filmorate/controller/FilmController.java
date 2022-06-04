package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if(films.containsKey(film.getId())) {
            throw new FilmAlreadyExistException("Фильм с названием " +
                    film.getName() + " уже добавлен.");
        }
        String checkFilm = validationFilm(film);
        if(!(checkFilm.isBlank())){
            throw new ValidationException(checkFilm);
        }
        film.setId(++Film.countFilm);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        String checkFilm = validationFilm(film);
        if(!(checkFilm.isBlank())){
            throw new ValidationException(checkFilm);
        }
        if(film.getId() < 1){
            throw new ValidationException("не корректный id film");
        }
        films.put(film.getId(), film);
        return film;
    }

    private String validationFilm(Film film) {
        List<String> result = new ArrayList<>();
        if(film.getName() == null || film.getName().isBlank()) {
            result.add("название фильма не может быть пустым");
        }
        if(film.getDescription().length()>200){
            result.add("максимальная длина описания более 200 символов");
        }
        LocalDate BirthdayFilms = LocalDate.parse("28 12 1895",
                DateTimeFormatter.ofPattern("dd MM yyyy"));
        if(BirthdayFilms.isAfter(film.getReleaseDate())){
            result.add("дата релиза раньше 28 декабря 1895 года");
        }
        if(film.getDuration() < 0){
            result.add("продолжительность фильма должна быть положительной");
        }
        return String.join(", ", result);
    }


}
