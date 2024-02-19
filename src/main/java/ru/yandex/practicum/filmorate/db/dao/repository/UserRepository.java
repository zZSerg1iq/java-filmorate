package ru.yandex.practicum.filmorate.db.dao.repository;

import ru.yandex.practicum.filmorate.db.dao.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> getUser(long userId);

    List<User> getUserList();

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(long userId);

    boolean addFriend(long userId, long friendId);

    boolean deleteFriend(long userId, long friendId);

    List<User> getFriendList(long userId);

}
