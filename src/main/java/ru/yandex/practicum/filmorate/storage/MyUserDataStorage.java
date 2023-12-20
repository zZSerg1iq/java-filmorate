package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class MyUserDataStorage implements UserDataStorage {

    private final Map<Long, User> userDataStorage;
    private long id = 1;


    public MyUserDataStorage() {
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
        if (user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        user.setId(id++);
        userDataStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userDataStorage.containsKey(user.getId())){
            if (user.getName().isBlank()){
                user.setName(user.getLogin());
            }
            userDataStorage.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public void deleteUser(long id) {
        User user = userDataStorage.get(id);
        if (user != null){
            userDataStorage.remove(id);
        }
    }
}
