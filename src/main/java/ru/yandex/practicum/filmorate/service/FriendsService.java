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
}
