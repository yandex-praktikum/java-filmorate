package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        setNewUserId(user);
        putUser(user);
        log.debug("Created user {}", user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId()))
            throw new ValidationException("User with id " + user.getId() + " not exists");
        putUser(user);
        log.debug("Updated user {}", user);
        return user;
    }

    private void putUser(User user) {
        checkCorrect(user);
        setNameIfNullOrBlank(user);
        users.put(user.getId(), user);
    }

    private void checkCorrect(User user) {
        StringBuilder sb = new StringBuilder();

        if (user.getEmail().isBlank() || !user.getEmail().contains("@"))
            sb.append("Email shouldn't be blank and should contain '@'.");
        if (user.getLogin().isBlank() || user.getLogin().contains(" "))
            sb.append("Login shouldn't be blank and shouldn't contain spaces.");
        if (user.getBirthday().isAfter(LocalDate.now()))
            sb.append("Birthdate shouldn't be in the future.");

        if (sb.length() > 0){
            log.debug(sb.toString());
            throw new ValidationException(sb.toString());
        }
    }

    private void setNewUserId(User user) {
        int id = 0;
        if (!users.isEmpty())
            id = Collections.max(users.keySet());
        log.debug("Set user id {}", id);
        user.setId(id + 1);
    }

    private void setNameIfNullOrBlank(User user) {
        if (user.getName() == null || user.getName().isBlank()){
            log.debug("Set user name of login {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }


}
