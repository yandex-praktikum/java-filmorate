package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    /**
     * Method returns all users.
     *
     * @return List of all users.
     */
    List<User> findAll();

    /**
     * Method returns user from repository by id.
     *
     * @param id Id of user to be found.
     *
     * @return User with requested id.
     */
    User findById(Integer id);

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
