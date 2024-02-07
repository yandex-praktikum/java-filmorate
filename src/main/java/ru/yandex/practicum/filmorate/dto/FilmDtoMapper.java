package ru.yandex.practicum.filmorate.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class FilmDtoMapper {

    public Film dtoToFilm(FilmDto filmDto) {
        Film film = new Film();
        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setDuration(filmDto.getDuration());
        film.setReleaseDate(filmDto.getReleaseDate());
        return film;
    }

    public FilmDto filmToDto(Film film) {
        if (film != null) {
            FilmDto filmDto = new FilmDto(film.getReleaseDate(), film.getDuration());
            filmDto.setId(film.getId());
            filmDto.setName(film.getName());
            filmDto.setDescription(film.getDescription());
            filmDto.setDuration(film.getDuration());
            filmDto.setReleaseDate(film.getReleaseDate());
            return filmDto;
        }
        return null;
    }
}
