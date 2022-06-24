package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final FilmService filmService;

    @Autowired
    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<RatingMPA> getAllMpa() {
        log.info("Выполнен запрос getAllMpa.");
        return filmService.getAllMpa();
    }

    @GetMapping("/{id}")
    public RatingMPA getMpa(@PathVariable Optional<Long> id) {
        if (id.isEmpty())
            throw new ValidationException("id");
        log.info("Выполнен запрос getMpa.");
        return filmService.getMpa(id.get());
    }
}
