package ru.yandex.practicum.filmorate.db.dto.servise.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.dao.entity.User;
import ru.yandex.practicum.filmorate.db.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.db.dto.entity.UserDto;
import ru.yandex.practicum.filmorate.db.dto.servise.UserRepositoryService;
import ru.yandex.practicum.filmorate.db.mapping.UserMapper;
import ru.yandex.practicum.filmorate.exception.DataConflictException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserRepositoryServiceImpl implements UserRepositoryService {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUserById(long userId) {
        Optional<User> userOpt = userRepository.getUserById(userId);

        if (userOpt.isEmpty()) {
            throw new DataNotFoundException("Пользователя с id " + userId + " не существует");
        }

        return new UserMapper().entityToFullDto(userOpt.get());
    }

    @Override
    public List<UserDto> getUserList() {
        return new UserMapper().entityToFullDtoList(userRepository.getUserList());
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        UserMapper userMapper = new UserMapper();

        if (userDto.getName() == null || userDto.getName().isBlank()) {
            userDto.setName(userDto.getLogin());
        }

        User user = userRepository.addUser(userMapper.dtoToEntity(userDto));
        log.warn("Добавлен новый пользователь: " + userDto.getLogin());

        return userMapper.entityToSimpleDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        Optional<User> optionalUser = userRepository.getUserById(userDto.getId());
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Внутренняя ошибка: Ошибка обновления данных пользователя с id " + userDto.getId() + ". Пользователя не существует.");
        }

        if (userDto.getName() == null || userDto.getName().isBlank()) {
            userDto.setName(userDto.getLogin());
        }

        log.warn("Изменены данные пользователя: " + userDto.getLogin());
        userRepository.updateUser(new UserMapper().dtoToEntity(userDto));
        return userDto;
    }

    @Override
    public void deleteUser(long userId) {
        Optional<User> userOpt = userRepository.getUserById(userId);
        if (userOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка удаления пользователя с id " + userId + ". Пользователя не существует.");
        }

        log.warn("Пользователь удален: " + userOpt.get().getLogin());
        userRepository.deleteUser(userId);
    }

    @Override
    public UserDto addFriend(long userId, long friendId) {
        Optional<User> userOpt = userRepository.getUserById(userId);
        Optional<User> friendOpt = userRepository.getUserById(friendId);

        if (userOpt.isEmpty()) {
            throw new InternalDataException("Внутренняя ошибка: Ошибка добавления в друзья к пользователю с id " + userId + ". Пользователя не существует.");
        }
        if (friendOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка добавления пользователя с id " + friendId + " в список друзей пользователя " + userId + ". Пользователь не найден.");
        }

        User user = userOpt.get();
        User friend = friendOpt.get();

        if (user.getFriendLists().contains(friend)) {
            throw new DataConflictException("Конфликт добавления в друзья пользователя с id " + friendId + ". Пользователь уже находится в списке друзей");
        }

        // если это принятие предлагаемой дружбы
        if (user.getFriendRequestList().contains(friend)) {
            userRepository.confirmFriendRequest(userId, friendId);
        } else { // если это предложение дружбы
            userRepository.addFriend(userId, friendId);
        }

        log.warn("Новые друзья: " + user.getLogin() + " & " + friend.getLogin());

        user.getFriendLists().add(friend);
        return new UserMapper().entityToFullDto(user);
    }

    @Override
    public UserDto deleteFriend(long userId, long friendId) {
        Optional<User> userOpt = userRepository.getUserById(userId);
        Optional<User> friendOpt = userRepository.getUserById(friendId);

        if (userOpt.isEmpty()) {
            throw new InternalDataException("Внутренняя ошибка: Ошибка удаления из друзей у пользователя с id " + userId + ". Пользователя не существует.");
        }
        if (friendOpt.isEmpty()) {
            throw new InternalDataException("Внутренняя ошибка: Ошибка удаления из друзей у пользователя с id " + friendId + ". Пользователя не существует.");
        }

        User user = userOpt.get();
        User friend = friendOpt.get();

        if (userRepository.deleteFriendFromUser(userId, friendId) == 0) {
            throw new DataNotFoundException("Ошибка удаления пользователя с id " + friendId + " из списка друзей. Пользователя нет с списке друзей");
        }

        if (friend.getFriendRequestList().contains(user)) {
            userRepository.deleteFriendRequests(userId, friendId);
        }


        log.warn("Прекращены дружба: " + user.getLogin() + " & " + friend.getLogin());
        user.getFriendLists().remove(friend);

        return new UserMapper().entityToFullDto(user);
    }

    @Override
    public List<UserDto> getFriendList(long userId) {
        Optional<User> userOpt = userRepository.getUserById(userId);
        if (userOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка получения списка друзей: пользователя с id " + userId + " не существует");
        }
        List<User> friendList = userOpt.get().getFriendLists();

        return new UserMapper().entityToFullDtoList(friendList);
    }

    @Override
    public List<UserDto> getCommonFriendList(long userId, long otherUserId) {
        Optional<User> userOpt = userRepository.getUserById(userId);
        Optional<User> otherOpt = userRepository.getUserById(otherUserId);
        if (userOpt.isEmpty()) {
            throw new DataNotFoundException("Внутренняя ошибка: Ошибка получения списка общих друзей. Пользователя с id " + userId + " не существует.");
        }
        if (otherOpt.isEmpty()) {
            throw new DataNotFoundException("Ошибка получения списка общих друзей: пользователя с id " + otherUserId + " не существует");
        }

        //получили листы
        List<User> userFriendsId = userOpt.get().getFriendLists();
        List<User> otherFriendsId = otherOpt.get().getFriendLists();

        //определили самый большой из них
        List<User> maxList = userFriendsId.size() >= otherFriendsId.size() ? userFriendsId : otherFriendsId;
        List<User> minList = userFriendsId.size() < otherFriendsId.size() ? userFriendsId : otherFriendsId;

        List<User> commonFriends = new ArrayList<>();
        for (User id : maxList) {
            if (minList.contains(id)) {
                commonFriends.add(id);
            }
        }

        return new UserMapper().entityToFullDtoList(commonFriends);
    }

}