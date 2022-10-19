package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import java.util.Date;

@Data
@ToString
public class Film {
    private int id;
    private String name;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
    private int duration;
}
