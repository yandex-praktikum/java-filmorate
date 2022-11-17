package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.validation.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private List<Film> filmsArrayList = new ArrayList<>();
    private int idCount = 1;

    @GetMapping("/films") // получение списка фильмов
    public List<Film> findAll() {
        filmsArrayList.addAll(films.values());
        return filmsArrayList;
    }

    @PostMapping(value = "/films") // добавление фильма
    public Film create(@RequestBody Film film) throws ValidationException {
        if(!validate(film)) {
            throw new ValidationException();
        } else {
            film.setId(idCount);
            idCount++;
            films.put(film.getId(), film);
        }

        return film;
    }

    @PutMapping(value = "/films") // обновление фильма
    public Film update(@RequestBody Film film) throws ValidationException {
        try{
            if (films.containsKey(film.getId())) {
            if(!validate(film)) {
                throw new ValidationException("Update was cancelled");
            } else {
                films.remove(film.getId());
                films.put(film.getId(), film);
            }
        } else {
            throw new ValidationException("Update was cancelled");
        }
    } catch (ValidationException exception){
        throw new ValidationException(exception.getMessage());
    }
        return film;
    }

    private boolean validate(Film film) throws ValidationException {
        boolean check = false;
        try{
            Optional<String> filmName = film.getName();
            if(filmName==null || filmName.get()=="") {
                throw new ValidationException("The name of the film can't be empty.");
            } else if (film.getDescription().length() > 200) {
                throw new ValidationException("The film description is too long.");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
            || film.getReleaseDate().isAfter(LocalDate.now())) {
                throw new ValidationException("The release data of the film can't be before 28.12.1895 " +
                        "and after today");
            } else if (film.getDuration() < 0) {
                throw new ValidationException("The film duration can't be under 0.");
            } else {
                check = true;
            }
        } catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage());
        }
        return check;
    }

}
