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
            log.info("Ошибка получения пользователя с id " + userId + ". Пользователь не найден");
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

        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        Optional<User> optionalUser = userStorage.getUser(user.getId());
        if (optionalUser.isEmpty()) {
            log.warn("Внутренняя ошибка: Ошибка обновления данных пользователя с id " + user.getId() + ". Пользователя не существует.");
            throw new InternalDataException("Внутренняя ошибка: Ошибка обновления данных пользователя с id " + user.getId() + ". Пользователя не существует.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        userStorage.updateUser(user);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        Optional<User> userOpt = userStorage.getUser(userId);
        if (userOpt.isEmpty()) {
            log.info("Ошибка удаления пользователя с id " + userId + ". Пользователя не существует.");
            throw new DataNotFoundException("Ошибка удаления пользователя с id " + userId + ". Пользователя не существует.");
        }

        userStorage.deleteUser(userId);
    }

    @Override
    public User addFriend(long userId, long friendId) {
        Optional<User> userOpt = userStorage.getUser(userId);
        Optional<User> friendOpt = userStorage.getUser(friendId);
        if (userOpt.isEmpty()) {
            log.warn("Внутренняя ошибка: Ошибка добавления в друзья к пользователю с id " + userId + ". Пользователя не существует.");
            throw new InternalDataException("Внутренняя ошибка: Ошибка добавления в друзья к пользователю с id " + userId + ". Пользователя не существует.");
        }
        if (friendOpt.isEmpty()) {
            log.info("Ошибка добавления пользователя с id " + friendId + " в список друзей пользователя " + userId + ". Пользователь не найден.");
            throw new DataNotFoundException("Ошибка добавления пользователя с id " + friendId + " в список друзей пользователя " + userId + ". Пользователь не найден.");
        }


        User user = userOpt.get();

        if (!user.addFriend(friendId)) {
            log.info("Конфликт добавления в друзья пользователя с id " + friendId + ". Пользователь уже находится в списке друзей");
            throw new DataConflictException("Конфликт добавления в друзья пользователя с id " + friendId + ". Пользователь уже находится в списке друзей");
        }
        userStorage.updateUser(userOpt.get());
        return user;
    }

    @Override
    public User deleteFriend(long userId, long friendId) {
        Optional<User> userOpt = userStorage.getUser(userId);
        if (userOpt.isEmpty()) {
            log.warn("Внутренняя ошибка: Ошибка удаления из друзей у пользователя с id " + userId + ". Пользователя не существует.");
            throw new InternalDataException("Внутренняя ошибка: Ошибка удаления из друзей у пользователя с id " + userId + ". Пользователя не существует.");
        }

        User user = userOpt.get();

        if (!user.deleteFriend(friendId)) {
            log.info("Ошибка удаления пользователя с id " + friendId + " из списка друзей. Пользователя нет с списке друзей");
            throw new DataNotFoundException("Ошибка удаления пользователя с id " + friendId + " из списка друзей. Пользователя нет с списке друзей");
        }
        userStorage.updateUser(user);
        return user;
    }

    @Override
    public List<User> getFriendList(long userId) {
        Optional<User> userOpt = userStorage.getUser(userId);
        if (userOpt.isEmpty()) {
            log.warn("Ошибка получения списка друзей: пользователя с id " + userId + " не существует");
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
        Optional<User> otherOpt = userStorage.getUser(userId);
        if (userOpt.isEmpty()) {
            log.warn("Внутренняя ошибка: Ошибка получения списка общих друзей. Пользователя с id " + userId + " не существует.");
            throw new DataNotFoundException("Внутренняя ошибка: Ошибка получения списка общих друзей. Пользователя с id " + userId + " не существует.");
        }
        if (otherOpt.isEmpty()) {
            log.info("Ошибка получения списка общих друзей: пользователя с id " + otherUserId + " не существует");
            throw new DataNotFoundException("Ошибка получения списка общих друзей: пользователя с id " + otherUserId + " не существует");
        }

        //получили листы
        Set<Long> userFriendsId = userOpt.get().getFriendIdList();
        Set<Long> otherFriendsId = userOpt.get().getFriendIdList();

        //определили самый большой из них
        Set<Long> mostBiggestList = userFriendsId.size() > otherFriendsId.size() ? userFriendsId : otherFriendsId;
        Set<Long> otherList = userFriendsId.size() < otherFriendsId.size() ? userFriendsId : otherFriendsId;

        List<User> commonFriends = new ArrayList<>();
        for (long id : mostBiggestList) {
            if (otherList.contains(id)) {
                commonFriends.add(userStorage.getUser(id).get());
            }
        }
        return commonFriends;
    }
}