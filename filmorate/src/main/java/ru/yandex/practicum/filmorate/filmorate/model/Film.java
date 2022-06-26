package ru.yandex.practicum.filmorate.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Film {
    public Film(int id, String name, String description, LocalDate releaseDate, int duration,
              List<Genre> genre, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genre;
    }
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();

    public Map<String, Object> toMap(){
        Map<String, Object> values = new HashMap<>();
        values.put("FILM_NAME", name);
        values.put("DESCRIPTION", description);
        values.put("RELEASE_DATE", releaseDate);
        values.put("DURATION", duration);
        values.put("RATING_ID", mpa.getId());
        return values;
    }
}
