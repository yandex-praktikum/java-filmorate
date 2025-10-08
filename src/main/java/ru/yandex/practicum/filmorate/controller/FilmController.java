package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody FilmDto filmDto) {
        Film film = FilmMapper.mapToFilm(filmDto);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody FilmDto filmDto) {
        Film film = FilmMapper.mapToFilm(filmDto);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film userLikesFilm(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return filmService.userLikesFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikesFilm(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        return filmService.deleteLikesFilm(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> listFirstCountFilm(@RequestParam(defaultValue = "10") int count) {
        return filmService.listFirstCountFilm(count);
    }

}