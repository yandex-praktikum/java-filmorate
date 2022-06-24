package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {


    List<User> getAllUsers();

    User getUser(Long id);

    User create(User user);

    User update(User user);
}
