package ru.yandex.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.exception.NotFoundException;
import ru.yandex.exception.ValidationException;
import ru.yandex.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Data
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        log.info("Получен запрос на создание пользователя {}", user);
        if (user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            log.error("Ошибка валидации email");
            throw new ValidationException("Введен имейл неверного формата");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка валидации login");
            throw new ValidationException("Введен неправильный логин");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.error("Ошибка валидации name");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации birthday");
            throw new ValidationException("День рождения не может быть в будушем");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создался новый пользователь");
        return user;
    }

    @Override
    public User deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            return users.remove(user.getId());
        }
        log.error("Ошибка валидации");
        throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
    }

    @Override
    public User updateUser(User newUser) {
        log.info("Получен запрос на обновление пользователя {}", newUser);
        if (newUser.getId() == null) {
            log.error("Ошибка валидации");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Пользователь обновился");
            return newUser;
        }
        log.error("Ошибка валидации");
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @Override
    public Collection<User> allUsers() {
        log.info("Получен запрос на получение пользователей");
        return users.values();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
