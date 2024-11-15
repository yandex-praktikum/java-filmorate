package ru.yandex.storage;

import ru.yandex.model.User;

import java.util.Collection;

public interface UserStorage {
    User createUser(User user);

    Collection<User> allUsers();

    User updateUser(User newUser);

    User deleteUser(User user);
}
