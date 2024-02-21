package ru.yandex.practicum.filmorate.db.dao.repository.impl;

import ru.yandex.practicum.filmorate.db.dao.entity.User;
import ru.yandex.practicum.filmorate.db.dao.repository.UserRepository;

import java.util.*;

//@Component
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> userDataStorage;

    private final Map<Long, Set<Long>> friendList;
    private long id = 1;


    public InMemoryUserRepository() {
        userDataStorage = new HashMap<>();
        friendList = new HashMap<>();
    }

    @Override
    public Optional<User> getUserById(long userId) {
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
        friendList.remove(userId);
        userDataStorage.remove(id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
       if (!friendList.containsKey(userId)) {
            friendList.put(userId, new HashSet<>());
        }
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        if (friendList.isEmpty() || !friendList.containsKey(userId)) {
            return false;
        }
        return friendList.get(userId).remove(friendId);
    }

    @Override
    public List<User> getFriendList(long userId) {
        List<User> userList = new ArrayList<>();
        if (userDataStorage.containsKey(userId)) {
            if (friendList.get(userId).size() > 0) {
                var friendSet = friendList.get(userId);
                for (Long id : friendSet) {
                    userList.add(userDataStorage.get(id));
                }
            }
        }
        return userList;
    }
}
