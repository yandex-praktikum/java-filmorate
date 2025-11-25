package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    User update(User user);

    Optional<User> findById(long id);

    Collection<User> findAll();

    void deleteById(long id);

    User addFriend(long userId, long friendId);

    User removeFriend(long userId, long friendId);

    Collection<User> findFriends(long userId);

    Collection<User> findMutualFriends(long userId, long otherUserId);
}

