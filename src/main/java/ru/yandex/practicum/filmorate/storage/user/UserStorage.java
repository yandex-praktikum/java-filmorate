package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User getUserById(Long id);

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    User createFriendship(long senderUserId, long receiverUserId);

    User deleteFriendship(long id, long friendId);

    public Collection<User> listOfFriends(long id);

    Collection<User> listOfCommonFriends(Long id, Long otherId);

}
