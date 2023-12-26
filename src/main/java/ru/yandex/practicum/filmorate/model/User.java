package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private String login;
    private String email;
    private String name;
    private LocalDate birthday;
    private int id;
}
