package controllers;

import jakarta.validation.Valid;
import lombok.Data;

import lombok.extern.slf4j.Slf4j;
import model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private List<Film> films = new ArrayList<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film){
        films.add(film);
        return film;
    }

    @PatchMapping
    public Film updateFilm(@Valid @RequestBody Film film){
        films.add(film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms(){
        return films;
    }
}
