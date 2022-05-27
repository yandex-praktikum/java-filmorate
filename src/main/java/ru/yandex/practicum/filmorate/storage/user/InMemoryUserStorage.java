package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;
    private static long idCounter = 0;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    private static long createID() {
        return ++idCounter;
    }
    @Override
    public User addUser(User user) {
        if (users.values().stream()
                .filter(x -> x.getLogin().equalsIgnoreCase(user.getLogin()))
                .anyMatch(x -> x.getEmail().equalsIgnoreCase(user.getEmail()))) {
            log.error("Пользователь '{}' с элетронной почтой '{}' уже существует.",
                    user.getLogin(), user.getEmail());
            throw new ValidationException("This user already exists");
        }
        isValid(user);
        if(user.getId() == null) user.setId(createID());
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User deleteUser(User user) {
        return users.remove(user.getId());
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())){
            log.error("Пользователь '{}' c id '{}' не найден", user.getLogin(), user.getId());
            throw new UserNotFoundException(
                    String.format("Пользователь с id:'%d' не найден.", user.getId()));
        }
        if (isValid(user)) {
            users.put(user.getId(), user);
            log.info("Данные пользователя '{}' обновлены", user.getLogin());
        }
        return user;
    }

    @Override
    public Map<Long, User> getAllUsers() {
        return users;
    }

    @Override
    public User getUser(Long id) {
        if(users.get(id) == null) {
            log.error("Пользователь с id '{}' не найден.", id);
            throw new UserNotFoundException(
                    String.format("Пользователь с id:'%d' не найден.", id)
            );
        }
        return users.get(id);
    }


    private boolean isValid(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Некорректный адрес электронной почты");
            throw new ValidationException("invalid email");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Логин не должен быть пустым и не должен содержать пробелов");
            throw new ValidationException("invalid login");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("invalid birthday");
        } else {
            if (user.getName().isBlank()) user.setName(user.getLogin());
            return true;
        }
    }
}
