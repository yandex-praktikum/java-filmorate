package filmorate.controller;

import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private int startedId = 1;
    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.parse("1895-12-28");
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.debug("Количество фильмов до добавления: {}", films.size());
        if (film.getName().equals("") || film.getDescription().length() > 200
                || film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)
                || film.getDuration() <= 0) {
            log.debug("Переданы некорректные данные.");
            throw new ValidationException("Проверьте данные и сделайте повторный запрос.");
        }
        film.setId(createId());
        films.put(film.getId(), film);
        log.debug("Количество фильмов после добавления: {}", films.size());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Фильм с таким id не найден.");
        }
        films.put(film.getId(), film);
        log.debug("Информация о фильме успешно обновлена.");
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private int createId() {
        return startedId++;
    }
}