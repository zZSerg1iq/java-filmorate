package ru.yandex.practicum.filmorate.db.dao.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.entity.User;
import ru.yandex.practicum.filmorate.db.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.db.enums.FriendRequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class H2UserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private final String getUserByIdQuery = "select * from _users where id = ?";
    private final String getUserFriendListQuery =
            "select u.id, u._name, u.login, u.birthday, u.email from friend_list fl " +
                    "inner join _users u on u.id = fl.user_id " +
                    "where user_id = ? and friend_request_status like ?";

    private final String getUserLikesQuery =
            "select u.id, u._name, u.login, u.birthday, u.email " +
                    "from user_likes ul " +
                    "inner join _users u on u.id = ul.user_id " +
                    "where ul.User_id = ?";

    private final String findUserByDataQuery =
            "select * from Users f " +
                    "where f._name like CONCAT('%', ? '%') " +
                    "and f.RELEASE_DATE = ?";

    private final String getUserListQuery = "select * from Users";

    private final String createUserQuery =
            "INSERT INTO Users(_name, description, release_date, duration, genre, mpa_rate) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private final String updateUserByIdQuery = "UPDATE User SET _name = ?, description = ?, release_date = ?, duration = ?, genre = ?, mpa_rate = ? WHERE id = ?";
    private final String deleteUserByIdQuery = "delete from Users where id = ?";
    private final String deleteUserUserLikes = "delete from user_likes where User_id = ?";

    private final String addUserLikeQuery = "insert into user_likes(user_id, User_id) values(?, ?)";
    private final String deleteUserLikeQuery = "delete from user_likes where user_id = ? and User_id = ?";

    @Override
    public Optional<User> getUser(long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(getUserByIdQuery, userId);
        if (userRows.next()) {
            return Optional.of(getUserFromRow(userRows, userId));
        }
        return Optional.empty();
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    @Override
    public User addUser(User user) {
        FriendRequestStatus.valueOf("CONFIRMED");

        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(long userId) {

    }

    @Override
    public boolean addFriend(long userId, long friendId) {
        return false;
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        return false;
    }

    @Override
    public List<User> getFriendList(long userId) {
        return null;
    }


    private User getUserFromRow(SqlRowSet rowSet, long userId) {
        return User.builder()
                .id(rowSet.getLong("ID"))
                .name(rowSet.getString("_NAME"))
                .login(rowSet.getString("login"))
                .birthday(rowSet.getDate("birthday"))
                .email(rowSet.getString("email"))
                .friendLists(getSubList(userId, "CONFIRMED"))
                .friendRequestList(getSubList(userId, "UNCONFIRMED"))
                .build();
    }

    private List<User> getSubList(long userId, String friendRequestStatus) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(getUserFriendListQuery, userId, friendRequestStatus);

        List<User> userList = new ArrayList<>();
        while (userRows.next()) {

            userList.add(
                    User.builder()
                            .id(userRows.getLong("ID"))
                            .name(userRows.getString("_name"))
                            .login(userRows.getString("LOGIN"))
                            .birthday(userRows.getDate("birthday"))
                            .email(userRows.getString("email"))
                            .build()
            );
        }
        return userList;
    }
}
