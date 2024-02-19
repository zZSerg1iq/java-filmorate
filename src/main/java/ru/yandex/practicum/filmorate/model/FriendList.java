package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.enums.FriendRequestStatus;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendList {

    private long id;

    private User user;

    private User friend;

    private FriendRequestStatus friendRequestStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendList that = (FriendList) o;
        return Objects.equals(user, that.user) && Objects.equals(friend, that.friend) && friendRequestStatus == that.friendRequestStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend, friendRequestStatus);
    }
}
