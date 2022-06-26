package ru.yandex.practicum.filmorate.filmorate.storage.impl.memory;

import lombok.Getter;
import ru.yandex.practicum.filmorate.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.filmorate.model.User;
import ru.yandex.practicum.filmorate.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Getter
//@Component
//@Qualifier("InMemoryUserStorage")
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
            throw new ObjectNotFoundException("Указанного пользователя не существует");
        }
        users.put(user.getId(), user);
        return user;
    }

    private void assignId(User user) {
        user.setId(id);
        id++;
    }
}
