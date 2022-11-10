package filmorate.models;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data

public class Film {

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public Film(int id, String name, String description, String releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = LocalDate.parse(releaseDate, DateTimeFormatter.ISO_DATE);
        this.duration = duration;
    }
}
