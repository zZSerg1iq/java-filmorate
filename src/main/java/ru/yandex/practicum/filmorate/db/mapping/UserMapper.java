package ru.yandex.practicum.filmorate.db.mapping;

import ru.yandex.practicum.filmorate.db.dto.entity.UserDto;
import ru.yandex.practicum.filmorate.db.dao.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public List<UserDto> entityToFullDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        if (userList != null && userList.size() > 0) {
            for (User user : userList) {
                userDtoList.add(entityToFullDto(user));
            }
        }
        return userDtoList;
    }

    public UserDto entityToFullDto(User user) {
        UserDto userDto = entityToSimpleDto(user);

        if (user.getFriendLists().size() > 0) {
            userDto.setFriendList(getFriendList(user.getFriendLists()));
        }
        if (user.getFriendRequestList().size() > 0) {
            userDto.setFriendRequestList(getFriendRequestList(user.getFriendRequestList()));
        }
        return userDto;
    }

    public UserDto entityToSimpleDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setBirthday(  user.getBirthday() );
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public User dtoToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setLogin(userDto.getLogin());
        user.setBirthday(userDto.getBirthday());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    private List<UserDto> getFriendList(List<User> friendLists) {
        List<UserDto> friendListDto = new ArrayList<>();
        for (User user : friendLists) {
            UserDto userDto = entityToSimpleDto(user);
            friendListDto.add(userDto);
        }
        return friendListDto;
    }

    private List<UserDto> getFriendRequestList(List<User> friendRequestList) {
        return getFriendList(friendRequestList);
    }


}
