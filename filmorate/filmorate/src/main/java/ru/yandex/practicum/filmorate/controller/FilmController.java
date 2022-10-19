package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id=0;

    @PostMapping("/films")
    public Film addNewFilm(@RequestBody Film film) throws ValidationException {
        validate(film);
        if(films.containsKey(film.getId())){
            throw new ValidationException("Фильм - уже есть в базе", film.getId());
        }else {
            id++;
            film.setId(id);
            films.put(id, film);
            return film;
        }
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        validate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        }else{
            throw new ValidationException("Фильма нет в базе", film.getId());
        }
    }

    @GetMapping("/films")
    public List<Film> getFilmsList() {
        return new ArrayList<>(films.values());
    }

    private void validate(Film film) throws ValidationException {
        boolean isNameEmpty = film.getName() == null || film.getName().isEmpty();
        log.info("---> size: "+film.getDescription().length());
        boolean isNameTooLong = film.getDescription().length() > 200;

        Date filmBirthDate = getDateFromString("28-12-1895");
        boolean isReleaseDateIsTooEarly = film.getReleaseDate().before(filmBirthDate);

        boolean isDurationCorrect = film.getDuration() < 0;

        if (isNameEmpty) {
            throw new ValidationException("название не может быть пустым;", film.getId());
        } else if (isNameTooLong) {
            throw new ValidationException("максимальная длина описания — 200 символов;", film.getId());
        }

        if (isReleaseDateIsTooEarly) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года;", film.getId());
        }

        if (isDurationCorrect) {
            throw new ValidationException("продолжительность фильма должна быть положительной;", film.getId());
        }
    }


    private Date getDateFromString(String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
