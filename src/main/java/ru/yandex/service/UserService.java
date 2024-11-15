package ru.yandex.service;


import ru.yandex.model.User;

import java.util.Collection;

public interface UserService {
    User createUser(User user);
    User deleteUser(User user);
    User updateUser(User newUser);
    Collection<User> allUsers();
    User getUserId(Long id);
    Collection<Long> generalFriends(Long id, Long otherId);
    Collection<Long> allFriends(Long id);
    User deleteFriend(Long id, Long friendId);
    User addFriend(Long id, Long friendId);
}
