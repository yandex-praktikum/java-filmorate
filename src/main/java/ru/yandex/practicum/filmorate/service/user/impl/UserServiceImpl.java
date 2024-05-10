package ru.yandex.practicum.filmorate.service.user.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users = new HashMap<>();
    private Long currentId = 1L;

    @Override
    public User createUser(User user) {

        Long id = currentId++;
        user.setId(id);
        users.put(id, user);
        log.info("Created new user with ID: {} and details: {}", id, user);
        return user;
    }

    @Override
    public User updateUser(Long id, User user) {

        if (users.containsKey(id)) {
            user.setId(id);
            users.put(id, user);
            log.info("Updated user with ID: {} and details: {}", id, user);
            return user;
        } else {
            log.warn("Attempted to update non-existing user with ID: {}", id);
            return null;
        }
    }


    @Override
    public List<User> getAllUsers() {
        log.debug("Fetching all users");
        return new ArrayList<>(users.values());
    }
}