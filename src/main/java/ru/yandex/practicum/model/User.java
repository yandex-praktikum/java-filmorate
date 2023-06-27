package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class User {

    @NonNull
    private final int id;

    @NonNull
    private String email;

    @NonNull
    private String login;

    @NonNull
    private String name;

    @NonNull
    private LocalDate birthday;
}
