package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int id;//целочисленный идентификатор — id;
    private String email; //электронная почта — email;
    private String login;//логин пользователя — login;
    private String name;//имя для отображения — name;
    private LocalDate birthday;//дата рождения — birthday.
}