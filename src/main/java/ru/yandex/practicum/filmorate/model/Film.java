package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private Integer id;
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(max = 255, min = 1, message = "Максимальная длина имени - 255 символов")
    private String name;
    @Size(max = 200, min = 1, message = "Максимальная длина описания - 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательной")
    private Integer duration;
    private Set<Integer> likes;
}
