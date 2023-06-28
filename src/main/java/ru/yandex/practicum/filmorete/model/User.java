package ru.yandex.practicum.filmorete.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class User {

    private Integer id;

    @NonNull
    private String name;

    @NonNull
    private LocalDate birthday;

    @NonNull
    private String login;

    @NonNull
    private String email;
}
