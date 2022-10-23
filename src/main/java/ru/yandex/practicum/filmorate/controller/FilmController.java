package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.validation.Validation.checkId;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms(){
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable Integer id){
        checkId(id);
        return filmService.findFilm(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film){
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film){
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public String deleteFilm(@PathVariable Integer id){
        checkId(id);
        return filmService.deleteFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public String putLike(@PathVariable Integer id, @PathVariable Integer userId){
        checkId(id, userId);
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLike(@PathVariable Integer id, @PathVariable Integer userId){
        checkId(id, userId);
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count){
        if (count < 1) {
            throw new ValidationException("Введите положительное число 'count'.");
        }
        return filmService.getTopFilms(count);
    }

}
