package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsService {

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
}
