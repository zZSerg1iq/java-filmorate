package ru.yandex.practicum.filmorate.servise;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorageService {

    User getUser(long userId);

    List<User> getUserList();

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(long userId);

    User addFriend(long userId, long friendId);

    User deleteFriend(long userId, long friendId);

    List<User> getFriendList(long userId);

    List<User> getCommonFriendList(long userId, long otherUserId);
}
