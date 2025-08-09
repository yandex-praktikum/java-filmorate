package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;
import java.util.Date;

@Data
public class Film {

    private int id;
    @NotBlank(message = "Название не может быть пустым.")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;
    @PastOrPresent(message = "Дата релиза не может быть больше текущей даты.")
    private Date releaseDate;
    @DurationMin(message = "Продолжительность фильма должна быть положительным числом.", nanos = 1)
    private Duration duration;

}
