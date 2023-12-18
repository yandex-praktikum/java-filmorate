package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Data
@Slf4j
public class FilmController {

    private Integer globalId = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate EARLIEST_FILM = LocalDate.of(1895, 12, 28);
    private final MessageSource messageSource;

    @GetMapping
    public List<Film> findAll() {
        log.debug("List off all films has been requested.");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film){
        validateFilmReleaseDate(film.getReleaseDate());

        film.setId(globalId);
        films.put(globalId, film);
        globalId++;

        log.debug("Film with id = {} has been created.", film.getId());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        int id = film.getId();

        checkFilmExist(id);
        films.put(id, film);

        log.debug("Film with id = {} has been updated.", film.getId());
        return film;
    }

    private void checkFilmExist(Integer id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Film with id %d does not exist!", id));
        }
    }

    private void validateFilmReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(EARLIEST_FILM)) {
            final String message = "Film release date should not be earlier that 28.12.1985.";
            log.warn(message);
            throw new BadRequestException(message);
        }
    }
}
