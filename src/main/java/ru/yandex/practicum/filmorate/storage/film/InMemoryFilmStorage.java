package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final HashMap<Integer, Film> films = new HashMap<>();

    public HashMap<Integer, Film> getFilms() {
        return films;
    }
    private List<Film> filmsArrayList = new ArrayList<>();
    private int idCount = 1;
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    public List<Film> findAll() {
        filmsArrayList.addAll(films.values());
        return filmsArrayList;
    }

    public Film create (@Valid Film film) throws ValidationException {
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

    public Film update(@Valid Film film) throws ValidationException {
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
