package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validation.UserValid;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.*;

@Data
public class User {
    private int id;
    private String name;
    @UserValid
    private LocalDate birthday;
    @NonNull @Email private String email;
    @NonNull private String login;

    public User(int id, String name, LocalDate birthday, @NonNull String email, @NonNull String login) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.login = login;
    }

   private Set<Long> friends = new HashSet<>();
    public void setFriends(int id) {
        friends.add((long) id);
    }

    public void deleteFriend(int id){
        friends.remove((long) id);
    }

}
