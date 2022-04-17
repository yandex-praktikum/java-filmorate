package ru.yandex.practicum.filmorate.validator;

import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@UtilityClass
public class FilmValidator {

    public void validate(Film film) {
        if(LocalDate.of(1895, 12,28).isAfter(film.getReleaseDate())){
            throw new ValidationException("Дата релиза фильма раньше 28.12.1895");
        }
    }
}
