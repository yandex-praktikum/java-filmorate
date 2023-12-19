package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    /**
     * Method returns all users.
     *
     * @return List of all users.
     */
    List<User> findAll();

    /**
     * Method adds user to repository.
     *
     * @param user User to be added.
     *
     * @return Added user with assigned ID.
     */
    User create(User user);

    /**
     * Method updates existing user in the repository.
     *
     * @param user User to be updated.
     *
     * @return Updated user.
     */
    User update(User user);
}
