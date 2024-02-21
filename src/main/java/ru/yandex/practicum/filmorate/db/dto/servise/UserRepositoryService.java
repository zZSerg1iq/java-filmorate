package ru.yandex.practicum.filmorate.db.dto.servise;


import ru.yandex.practicum.filmorate.db.dto.entity.UserDto;

import java.util.List;

public interface UserRepositoryService {

    UserDto getUserById(long userId);

    List<UserDto> getUserList();

    UserDto addUser(UserDto user);

    UserDto updateUser(UserDto user);

    void deleteUser(long userId);

    UserDto addFriend(long userId, long friendId);

    UserDto deleteFriend(long userId, long friendId);

    List<UserDto> getFriendList(long userId);

    List<UserDto> getCommonFriendList(long userId, long otherUserId);
}
