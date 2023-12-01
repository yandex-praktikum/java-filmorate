package ru.yandex.practicum.filmorate.model;

<<<<<<< HEAD
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NonNull private String name;
    @NonNull private String description;
    @NonNull private LocalDate releaseDate;
    @NonNull private int duration;
=======
import lombok.Getter;
import lombok.Setter;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
>>>>>>> fe5308a283657e62f7304ac14ae2e1ab40ddfac5
}
