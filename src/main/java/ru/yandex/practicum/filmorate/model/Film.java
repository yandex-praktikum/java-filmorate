package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class Film {

    private int id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private int duration;


}
