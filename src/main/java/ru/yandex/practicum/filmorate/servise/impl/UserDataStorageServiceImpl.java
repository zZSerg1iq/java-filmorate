package ru.yandex.practicum.filmorate.servise.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servise.UserDataStorageService;
import ru.yandex.practicum.filmorate.storage.UserDataStorage;
import java.util.List;

@Service
public class UserDataStorageServiceImpl implements UserDataStorageService {

    private final UserDataStorage userDataStorage;

    @Autowired
    public UserDataStorageServiceImpl(UserDataStorage userDataStorage) {
        this.userDataStorage = userDataStorage;
    }

    @Override
    public User getUser(long userId) {
        return userDataStorage.getUser(userId).orElseGet(User::new);
    }

    @Override
    public List<User> getUserList() {
        return userDataStorage.getUserList();
    }

    @Override
    public User addUser(User user) {
        return userDataStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userDataStorage.updateUser(user);
    }

    @Override
    public void deleteUser(long id) {
        userDataStorage.deleteUser(id);
    }
}
