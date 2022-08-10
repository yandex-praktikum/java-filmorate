package ru.yandex.prakticum.filmorate.controllers.films.users.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import java.util.Date;
@Data
@Builder
public class User {
    private final Integer id;
    private String email;
    private String login;
    private String nickname;
    private Date birthday;

}
