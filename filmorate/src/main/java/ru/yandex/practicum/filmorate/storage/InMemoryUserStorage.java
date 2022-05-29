package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Getter
@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;


    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        User user;
        if (users.containsKey(id)) {
            user = users.get(id);
        } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с id \"%s\"не существует.", id));
        }
        return user;
    }

    @Override
    public User create(User user) {
        if ((user.getName() == null || user.getName().equals(""))) {
            user.setName(user.getLogin());
        }
        if (user.getId() == 0) {
            assignId(user);
        } else {
            throw new ValidationException("Пользователь должен быть передан без id");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("Введите id пользователя, которого необходимо обновить");
        }
        if(!users.containsKey(user.getId())){
            throw new ObjectNotFoundException("Указанного фильма не существует");
        }
        users.put(user.getId(), user);
        return user;
    }

    private void assignId(User user) {
        user.setId(id);
        id++;
    }
}
