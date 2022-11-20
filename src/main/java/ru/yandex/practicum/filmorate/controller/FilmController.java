package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.validation.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.util.*;

@Validated
@RestController
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private List<Film> filmsArrayList = new ArrayList<>();
    private int idCount = 1;
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @GetMapping("/films") // получение списка фильмов
    public List<Film> findAll() {
        filmsArrayList.addAll(films.values());
        return filmsArrayList;
    }

    @PostMapping(value = "/films") // добавление фильма
    public Film create (@Valid @RequestBody Film film) throws ValidationException {
       try {
           Set<ConstraintViolation<Film>> validate = validator.validate(film);
           if (validate.size() > 0 || film.getName()=="") {
               throw new  ValidationException("Error while saving");
           } else {
               film.setId(idCount++);
               films.put(film.getId(), film);
           }
       } catch (ValidationException validationException) {
           throw new ValidationException(validationException.getMessage());
       }
        return film;
    }

    @PutMapping(value = "/films") // обновление фильма
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        try {
            Set<ConstraintViolation<Film>> validate = validator.validate(film);
            if (validate.size() > 0 || film.getName() == "") {
                throw new ValidationException("Error while updating");
            } else {
                if (films.containsKey(film.getId())) {
                    films.remove(film.getId());
                    films.put(film.getId(), film);
                } else {
                    throw new ValidationException("Error while updating");
                }
            }
        } catch (ValidationException validationException) {
            throw new ValidationException(validationException.getMessage());
        }
        return film;
    }
}
