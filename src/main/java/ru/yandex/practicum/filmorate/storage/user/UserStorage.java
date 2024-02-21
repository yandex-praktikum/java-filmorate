package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getFriends(int id);

    User addFriend(int id, int friendId);

    User deleteFriend(int id, int friendId);

    List<User> getCommonFriends(int id, int otherId);

    User createUser(User user);

    User updateUser(User user);

    List<User> getALlUsers();

    User getUser(int id);
}
