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

    /**
     * Method makes users with id and friendId friends.
     *
     * @param id Id of user adding a friend.
     * @param friendId Id of user to be friended.
     *
     * @return List of both new friends.
     */
    List<User> addFriend(Integer id, Integer friendId);

    /**
     * Method unfriends users with id and friendId.
     *
     * @param id Id of user deleting a friend.
     * @param friendId Id of user to be defriended.
     *
     * @return List of both ex-friends.
     */
    List<User> deleteFriend(Integer id, Integer friendId);

    /**
     * Method returns a list of user's friends.
     *
     * @param id Id of user requesting friends.
     *
     * @return List of user's friends.
     */
    List<User> getFriends(Integer id);

    /**
     * Method returns a list of user's common friends with other user.
     *
     * @param id Id of user requesting common friends.
     * @param otherId Id of user with whom the common friends are requested.
     *
     * @return List of user's common friends with other user.
     */
    List<User> getCommonFriends(Integer id, Integer otherId);

    /**
     * Method checks if user exist in repository and throws NotFoundException if data is not found.
     *
     * @param id Id of user to be checked.
     */
    void checkUserExist(Integer id);
}
