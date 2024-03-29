package ru.yandex.practicum.filmorate.db.dao.repository;

import ru.yandex.practicum.filmorate.db.dao.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> getUserById(long userId);

    List<User> getUserList();

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(long userId);

    void addFriend(long userId, long friendId);

    void confirmFriendRequest(long userId, long friendId);

    int deleteFriendFromUser(long userId, long friendId);

    int deleteFriendRequests(long userId, long friendId);

    List<User> getFriendList(long userId);


}
