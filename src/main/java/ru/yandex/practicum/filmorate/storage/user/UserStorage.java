package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> allUsers();
    User createUser(User user);
    User updateUser(User user);
    String deleteUser(int id);
    User findUser(int id);
}
