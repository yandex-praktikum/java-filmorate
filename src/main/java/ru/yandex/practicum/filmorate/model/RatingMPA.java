package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@EqualsAndHashCode
public class RatingMPA {
    @NotNull
    @Size(min = 1, max = 5)
    final private Long id;
    private String name;

    @JsonCreator
    public RatingMPA(Long id) {
        this.id = id;
    }

    public RatingMPA(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
