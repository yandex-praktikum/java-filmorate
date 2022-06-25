package ru.yandex.practicum.filmorate.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User {

    public User(int id, String name, @NonNull String email, @NonNull String login, LocalDate birthday,
                Set<Integer> friends, Set<Integer> favoriteFilms) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.friends = friends;
        this.favoriteFilms = favoriteFilms;
    }

    private int id;
    private String name;
    @NonNull
    @Email
    private String email;
    @NonNull
    @NotBlank
    @Pattern(regexp = "\\S*$")
    private String login;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
    private Set<Integer> favoriteFilms = new HashSet<>();

    public Map<String, Object> toMap(){
        Map<String, Object> values = new HashMap<>();
        values.put("USER_NAME", name);
        values.put("EMAIL", email);
        values.put("LOGIN", login);
        values.put("BIRTHDAY", birthday);
        return values;
    }
}
