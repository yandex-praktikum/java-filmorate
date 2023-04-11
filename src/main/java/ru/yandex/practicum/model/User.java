package ru.yandex.practicum.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс пользователей Filmorate
 */
@Data
public class User {
    private int id;
    @Email(message = "Please enter a valid email address")
    private String email;
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private Map<Integer, FriendStatus> friends = new HashMap<>();
}