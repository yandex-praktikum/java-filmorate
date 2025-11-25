package ru.yandex.practicum.filmorate.memory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
@Profile("mem")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(0);

    @Override
    public User create(User user) {
        user.setId(nextId.incrementAndGet());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        if (!users.containsKey(newUser.getId())) {
            throw new NotFoundException("User " + newUser.getId() + " not found");
        }
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void deleteById(long id) {
        if (users.remove(id) == null) {
            throw new NotFoundException("User " + id + " not found");
        }
    }

    @Override
    public User addFriend(long userId, long friendId) {
        User user = getOrThrow(userId);

        boolean added = user.getFriends().add(friendId);

        if (added) {
            users.put(userId, user);
        }
        return user;
    }

    @Override
    public User removeFriend(long userId, long friendId) {
        User user = getOrThrow(userId);

        boolean removed = user.getFriends().remove(friendId);

        if (removed) {
            this.update(user);
        }
        return user;
    }

    @Override
    public Collection<User> findFriends(long userId) {
        User user = getOrThrow(userId);
        return user.getFriends().stream()
                .map(this::getOrThrow)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> findMutualFriends(long userId, long otherUserId) {
        User user = getOrThrow(userId);
        User otherUser = getOrThrow(otherUserId);
        Set<Long> users = user.getFriends();
        Set<Long> otherUsers = otherUser.getFriends();

        List<User> result = new ArrayList<>();
        for (Long fid : users) {
            if (otherUsers.contains(fid)) {
                User friend = this.users.get(fid);
                if (friend != null) result.add(friend);
            }
        }
        return result;
    }

    private User getOrThrow(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("User " + id + " not found");
        }
        return user;
    }
}
