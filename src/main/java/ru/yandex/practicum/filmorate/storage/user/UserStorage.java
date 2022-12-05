package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> createUser(User user);

    Optional<User> updateUser(User user);

    List<User> getAll();

    void deleteUser(Long id);

    void deleteAll();

    Optional<User> getById(Long id);

    void addFriend(Long id, Long idFriend);

    void deleteFriend(Long id, Long idFriend);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long id, Long id2);
}
