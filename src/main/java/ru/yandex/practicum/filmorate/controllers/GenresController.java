package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenresController {
    private final FilmService filmService;

    @Autowired
    public GenresController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Genre> getAllGenres() {
        log.info("Выполнен запрос getAllGenres.");
        return filmService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable Optional<Long> id) {
        if (id.isEmpty())
            throw new ValidationException("id");
        log.info("Выполнен запрос getGenre.");
        return filmService.getGenre(id.get());
    }
}