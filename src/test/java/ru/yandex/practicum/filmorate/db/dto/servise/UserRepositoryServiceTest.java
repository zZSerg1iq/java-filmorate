package ru.yandex.practicum.filmorate.db.dto.servise;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.TestBasic;
import ru.yandex.practicum.filmorate.db.dto.entity.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryServiceTest extends TestBasic {

    @Test
    void getAndGetUserById() {
        UserDto userDto = userService.addUser(getRandomUserDto());

        UserDto userFromDB = userService.getUserById(userDto.getId());

        Throwable getFakeId_1 = assertThrows(RuntimeException.class, () -> userService.getUserById(-1));
        Throwable getFakeId_2 = assertThrows(RuntimeException.class, () -> userService.getUserById(9999));

        assertAll(
                () -> assertNotNull(userFromDB),
                () -> assertEquals(userDto, userFromDB),
                () -> assertEquals("Пользователя с id -1 не существует", getFakeId_1.getMessage()),
                () -> assertEquals("Пользователя с id 9999 не существует", getFakeId_2.getMessage())
        );
    }

    @Test
    void getUserList() {
        List<UserDto> userDtoList = new ArrayList<>();
        var emptyList = userService.getUserList();

        for (int i = 0; i < 5; i++) {
            userDtoList.add(userService.addUser(getRandomUserDto()));
        }

        var listWithUsers = userService.getUserList();

        assertAll(
                () -> assertNotNull(emptyList),
                () -> assertNotNull(listWithUsers),
                () -> assertEquals(5, listWithUsers.size()),
                () -> assertEquals(userDtoList, listWithUsers)
        );
    }

    @Test
    void updateUser() {
        UserDto user = userService.addUser(getRandomUserDto());

        user.setName(user.getName() + " updated");
        user.setLogin(user.getLogin() + " updated");
        user.setBirthday(user.getBirthday().minusYears(5));

        userService.updateUser(user);
        UserDto userFromDb = userService.getUserById(user.getId());

        Throwable noIdUser = assertThrows(RuntimeException.class, () -> userService.updateUser(getRandomUserDto()));


        assertAll(
                () -> assertNotNull(user),
                () -> assertNotNull(userFromDb),
                () -> assertEquals(user, userFromDb),
                () -> assertEquals("Внутренняя ошибка: Ошибка обновления данных пользователя с id 0. Пользователя не существует.", noIdUser.getMessage())
        );
    }

    @Test
    void deleteUser() {
        var listWithoutUser = userService.getUserList();
        UserDto user = userService.addUser(getRandomUserDto());

        var listWithUser = userService.getUserList();
        userService.deleteUser(user.getId());

        var listWithoutUser2 = userService.getUserList();

        Throwable unknownUser1 = assertThrows(RuntimeException.class, () -> userService.deleteUser(-1));
        Throwable unknownUser2 = assertThrows(RuntimeException.class, () -> userService.deleteUser(9999));

        assertAll(
                () -> assertNotNull(listWithoutUser),
                () -> assertNotNull(listWithUser),
                () -> assertNotNull(listWithoutUser2),

                () -> assertFalse(listWithoutUser.contains(user)),
                () -> assertTrue(listWithUser.contains(user)),
                () -> assertFalse(listWithoutUser2.contains(user)),

                () -> assertEquals("Ошибка удаления пользователя с id -1. Пользователя не существует.", unknownUser1.getMessage()),
                () -> assertEquals("Ошибка удаления пользователя с id 9999. Пользователя не существует.", unknownUser2.getMessage())
        );
    }

    @Test
    void addAndDeleteFriend() {
        long id1 = userService.addUser(getRandomUserDto()).getId();
        long id2 = userService.addUser(getRandomUserDto()).getId();

        userService.addFriend(id1, id2);
        UserDto userWithFriend = userService.getUserById(id1);
        UserDto userWithFriendRequest = userService.getUserById(id2);

        userService.addFriend(id2, id1);
        UserDto friend1 = userService.getUserById(id1);
        UserDto friend2 = userService.getUserById(id2);

        userService.deleteFriend(id1, id2);
        UserDto userWithFriendReq = userService.getUserById(id1);
        UserDto userWithOneFriend = userService.getUserById(id2);

        userService.deleteFriend(id2, id1);
        UserDto userWithNoFriendsAndRequests1 = userService.getUserById(id1);
        UserDto userWithNoFriendsAndRequests2 = userService.getUserById(id2);

        Throwable fakeUserIdAddFriend = assertThrows(RuntimeException.class, () -> userService.addFriend(999, id2));
        Throwable fakeFriendIdAddFriend = assertThrows(RuntimeException.class, () -> userService.addFriend(id1, 999));
        Throwable fakeFriendIdDeleteFriend = assertThrows(RuntimeException.class, () -> userService.deleteFriend(id1, 999));
        Throwable fakeUserIdDeleteFriend = assertThrows(RuntimeException.class, () -> userService.deleteFriend(999, id2));

        assertAll(
                () -> assertTrue(userWithFriend.getFriendList().contains(userWithFriendRequest)),
                () -> assertTrue(userWithFriendRequest.getFriendRequestList().contains(userWithFriend)),
                () -> assertFalse(userWithFriend.getFriendRequestList().contains(userWithFriendRequest)),
                () -> assertFalse(userWithFriendRequest.getFriendList().contains(userWithFriend)),

                () -> assertTrue(friend1.getFriendList().contains(friend2)),
                () -> assertTrue(friend2.getFriendList().contains(friend1)),
                () -> assertFalse(friend1.getFriendRequestList().contains(friend2)),
                () -> assertFalse(friend2.getFriendRequestList().contains(friend1)),

                () -> assertFalse(userWithFriendReq.getFriendList().contains(userWithOneFriend)),
                () -> assertTrue(userWithOneFriend.getFriendList().contains(userWithFriendReq)),
                () -> assertTrue(userWithFriendReq.getFriendRequestList().contains(userWithOneFriend)),
                () -> assertFalse(userWithOneFriend.getFriendRequestList().contains(userWithFriendReq)),

                () -> assertFalse(userWithNoFriendsAndRequests1.getFriendList().contains(userWithNoFriendsAndRequests2)),
                () -> assertFalse(userWithNoFriendsAndRequests2.getFriendList().contains(userWithNoFriendsAndRequests1)),
                () -> assertFalse(userWithNoFriendsAndRequests1.getFriendRequestList().contains(userWithNoFriendsAndRequests2)),
                () -> assertFalse(userWithNoFriendsAndRequests2.getFriendRequestList().contains(userWithNoFriendsAndRequests1)),

                () -> assertEquals("Внутренняя ошибка: Ошибка добавления в друзья к пользователю с id 999. Пользователя не существует.", fakeUserIdAddFriend.getMessage()),
                () -> assertEquals("Ошибка добавления пользователя с id 999 в список друзей пользователя 1. Пользователь не найден.", fakeFriendIdAddFriend.getMessage()),
                () -> assertEquals("Внутренняя ошибка: Ошибка удаления из друзей у пользователя с id 999. Пользователя не существует.", fakeFriendIdDeleteFriend.getMessage()),
                () -> assertEquals("Внутренняя ошибка: Ошибка удаления из друзей у пользователя с id 999. Пользователя не существует.", fakeUserIdDeleteFriend.getMessage())
        );
    }

    @Test
    void getCommonFriendList() {
        long id1 = userService.addUser(getRandomUserDto()).getId();
        long id2 = userService.addUser(getRandomUserDto()).getId();
        long id3 = userService.addUser(getRandomUserDto()).getId();
        long id4 = userService.addUser(getRandomUserDto()).getId();

        userService.addFriend(id1, id3);
        userService.addFriend(id2, id3);
        userService.addFriend(id2, id4);

        var commonFriend = userService.getUserById(id3);
        var onlyUser2Friend = userService.getUserById(id4);

        List<UserDto> commonFriendList = userService.getCommonFriendList(id1, id2);

        assertAll(
                () -> assertNotNull(commonFriendList),
                () -> assertEquals(1, commonFriendList.size()),
                () -> assertTrue(commonFriendList.contains(commonFriend)),
                () -> assertFalse(commonFriendList.contains(onlyUser2Friend))
        );

    }
}