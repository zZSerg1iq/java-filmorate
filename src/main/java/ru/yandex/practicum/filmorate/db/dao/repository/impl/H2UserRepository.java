package ru.yandex.practicum.filmorate.db.dao.repository.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.entity.User;
import ru.yandex.practicum.filmorate.db.dao.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
public class H2UserRepository implements UserRepository {

    @Override
    public Optional<User> getUser(long userId) {
        return Optional.empty();
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(long userId) {

    }

    @Override
    public boolean addFriend(long userId, long friendId) {
        return false;
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        return false;
    }

    @Override
    public List<User> getFriendList(long userId) {
        return null;
    }
}
