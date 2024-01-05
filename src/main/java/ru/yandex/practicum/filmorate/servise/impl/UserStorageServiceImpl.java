package ru.yandex.practicum.filmorate.servise.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataConflictException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalDataException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servise.UserStorageService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserStorageServiceImpl implements UserStorageService {

    private final UserStorage userStorage;

    @Autowired
    public UserStorageServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User getUser(long userId) {
        Optional<User> userOpt = userStorage.getUser(userId);
        if (userOpt.isEmpty()) {
            throw new DataNotFoundException("Пользователя с id " + userId + " не существует");
        }

        return userOpt.get();
    }

    @Override
    public List<User> getUserList() {
        return userStorage.getUserList();
    }

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setFriendIdList(new HashSet<>());

        log.warn("Добавлен новый пользователь: " + user.getLogin());
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        Optional<User> optionalUser = userStorage.getUser(user.getId());
        if (optionalUser.isEmpty()) {
            throw new InternalDataException("Внутренняя ошибка: Ошибка обновления данных пользователя с id " + user.getId() + ". Пользователя не существует.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        log.warn("Изменены данные пользователя: " + user.getLogin());
        userStorage.updateUser(user);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        Optional<User> userOpt = userStorage.getUser(userId);
        if (userOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка удаления пользователя с id " + userId + ". Пользователя не существует.");
        }

        log.warn("Пользователь удален: " + userOpt.get().getLogin());
        userStorage.deleteUser(userId);
    }

    @Override
    public User addFriend(long userId, long friendId) {
        Optional<User> userOpt = userStorage.getUser(userId);
        Optional<User> friendOpt = userStorage.getUser(friendId);
        if (userOpt.isEmpty()) {
            throw new InternalDataException("Внутренняя ошибка: Ошибка добавления в друзья к пользователю с id " + userId + ". Пользователя не существует.");
        }
        if (friendOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка добавления пользователя с id " + friendId + " в список друзей пользователя " + userId + ". Пользователь не найден.");
        }


        User user = userOpt.get();
        User friend = friendOpt.get();

        if (!user.addFriend(friendId)) {
            throw new DataConflictException("Конфликт добавления в друзья пользователя с id " + friendId + ". Пользователь уже находится в списке друзей");
        }
        friend.addFriend(userId);

        log.warn("Новые друзья: " + user.getLogin() + " & " + friend.getLogin());
        userStorage.updateUser(user);
        userStorage.updateUser(friend);

        return user;
    }

    @Override
    public User deleteFriend(long userId, long friendId) {
        Optional<User> userOpt = userStorage.getUser(userId);
        Optional<User> friendOpt = userStorage.getUser(friendId);

        if (userOpt.isEmpty()) {
            throw new InternalDataException("Внутренняя ошибка: Ошибка удаления из друзей у пользователя с id " + userId + ". Пользователя не существует.");
        }
        if (friendOpt.isEmpty()) {
            throw new InternalDataException("Внутренняя ошибка: Ошибка удаления из друзей у пользователя с id " + friendId + ". Пользователя не существует.");
        }

        User user = userOpt.get();
        User friend = friendOpt.get();

        if (!user.deleteFriend(friendId)) {
            throw new DataNotFoundException("Ошибка удаления пользователя с id " + friendId + " из списка друзей. Пользователя нет с списке друзей");
        }
        friend.deleteFriend(userId);

        log.warn("Прекращены дружба: " + user.getLogin() + " & " + friend.getLogin());
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        return user;
    }

    @Override
    public List<User> getFriendList(long userId) {
        Optional<User> userOpt = userStorage.getUser(userId);
        if (userOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка получения списка друзей: пользователя с id " + userId + " не существует");
        }

        Set<Long> friendsId = userOpt.get().getFriendIdList();
        List<User> friendList = new ArrayList<>();

        for (long id : friendsId) {
            friendList.add(userStorage.getUser(id).get());
        }
        return friendList;
    }

    @Override
    public List<User> getCommonFriendList(long userId, long otherUserId) {
        Optional<User> userOpt = userStorage.getUser(userId);
        Optional<User> otherOpt = userStorage.getUser(otherUserId);
        if (userOpt.isEmpty()) {
            throw new DataNotFoundException("Внутренняя ошибка: Ошибка получения списка общих друзей. Пользователя с id " + userId + " не существует.");
        }
        if (otherOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка получения списка общих друзей: пользователя с id " + otherUserId + " не существует");
        }


        //получили листы
        Set<Long> userFriendsId = userOpt.get().getFriendIdList();
        Set<Long> otherFriendsId = otherOpt.get().getFriendIdList();

        //определили самый большой из них
        Set<Long> maxList = userFriendsId.size() >= otherFriendsId.size() ? userFriendsId : otherFriendsId;
        Set<Long> minList = userFriendsId.size() < otherFriendsId.size() ? userFriendsId : otherFriendsId;

        List<User> commonFriends = new ArrayList<>();
        for (long id : maxList) {
            if (minList.contains(id)) {
                commonFriends.add(userStorage.getUser(id).get());
            }
        }

        return commonFriends;
    }
}