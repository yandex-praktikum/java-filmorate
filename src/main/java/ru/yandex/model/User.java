package ru.yandex.model;

import jakarta.validation.constraints.NotNull;
import jdk.jfr.Timestamp;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    @NotNull
    private String name;
    private Long id;
    @NotNull
    private String email;
    @NotNull
    private String login;
    @Timestamp()
    private LocalDate birthday;
    private Set<Long> friends;
    private int status;
}