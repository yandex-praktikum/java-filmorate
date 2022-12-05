package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getById(Long id);

    Optional<User> createUser(User user);

    Optional<User> updateUser(User user);

    List<User> getAll();

    void deleteUser(Long id);

    void deleteAll();

    void addFriend(Long id1, Long id2);

    void removeFriend(Long id1, Long id2);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long id, Long id2);


}
