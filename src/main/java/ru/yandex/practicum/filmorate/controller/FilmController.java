package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    FilmServiceImpl filmServiceImpl;


    @GetMapping
    public List<Film> getAllFIlms() {

        log.debug("There is {} films in filmorate", filmServiceImpl.getAllFilms().size());

        return  filmServiceImpl.getAllFilms()
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("Film with id={} added",film.getId());
        return filmServiceImpl.addFilms(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Film with id={} updated",film.getId());
        return filmServiceImpl.updateFilm(film);
    }


}
