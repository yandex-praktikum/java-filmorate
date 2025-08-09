package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final ArrayList<User> users = new ArrayList<>();
    private int currentId = 1;

    public User createUser(User user) {
        validateUser(user);
        user.setId(currentId);
        currentId++;
        users.add(user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    public User updateUser(int id, User updatedUser) {

        validateUser(updatedUser);

        Optional<User> existUser = users.
                                    stream().
                                    filter(user -> user.getId() == id).
                                    findFirst();

        if (existUser.isPresent()) {

            User user = existUser.get();
            user.setEmail(updatedUser.getEmail());
            user.setLogin(updatedUser.getLogin());
            user.setName(updatedUser.getName());
            user.setBirthday(updatedUser.getBirthday());
            log.info("Пользователь обновлен: {}", user);

            return user;

        } else {
            log.warn("Пользователь с указанным id {} не был найден", id);
            throw new ValidationException("Пользователь с таким id не найден.");
        }

    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    private void validateUser(User user) {

        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Ошибка валидации пользователя: электронная почта не может быть пустой и должна содержать символ '@'.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'!");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Ошибка валидации пользователя: логин не может быть пустым и содержать пробелы.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }

        if (user.getBirthday() == null || user.getBirthday().after(new Date())) {
            log.error("Ошибка валидации пользователя: дата рождения не может быть больше текущей даты или пустой.");
            throw new ValidationException("Дата рождения не может быть больше текущей даты или пустой!");
        }

    }

}
