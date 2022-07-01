package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ReleaseDate;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private static int count;
    private int id;
    @NotEmpty (message = "Имя не может быть пустым")
    private final String name;
    @Size(max = 200, message = "Описание должно быть не более 200 символов")
    private final String description;
    @ReleaseDate("1895-12-27")
    private final LocalDate releaseDate;
    @Positive
    private final long duration;

    public void generateId() {
        if (id == 0) {
            id = ++count;
        }
    }

}
