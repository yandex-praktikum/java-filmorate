package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> findAll();
    User getUser(int id);
    List<User> getUsers(Set<Integer> ids);
    User create(User user);
    User update(User user);
    void delete(User user);
}