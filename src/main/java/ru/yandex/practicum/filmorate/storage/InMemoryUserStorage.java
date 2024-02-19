package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userDataStorage;
    private long id = 1;


    public InMemoryUserStorage() {
        userDataStorage = new HashMap<>();
    }

    @Override
    public Optional<User> getUser(long userId) {
        return Optional.ofNullable(userDataStorage.get(userId));
    }

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(userDataStorage.values());
    }

    @Override
    public User addUser(User user) {
        user.setId(id++);
        userDataStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        return userDataStorage.put(user.getId(), user);
    }

    @Override
    public void deleteUser(long userId) {
        for (User user : userDataStorage.values()) {
            user.deleteFriend(userId);
        }

        userDataStorage.remove(id);
    }

}
