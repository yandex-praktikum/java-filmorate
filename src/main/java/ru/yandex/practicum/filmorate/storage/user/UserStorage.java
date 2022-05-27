package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    User addUser(User user);

    User deleteUser(User user);

    User updateUser(User user);

    Map<Long, User> getAllUsers();

    User getUser(Long id);
}
