package ru.yandex.practicum.filmorete.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {

    private Integer id;

    private String name;

    private LocalDate birthday;

    private String login;

    private String email;
}
