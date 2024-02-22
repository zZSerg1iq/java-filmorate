package ru.yandex.practicum.filmorate.db.dao.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.entity.User;
import ru.yandex.practicum.filmorate.db.dao.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class H2UserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String GET_USER_BY_ID = "select * from _users where id = ?";
    private final String GET_USER_FRIENDLIST =
            "select u.id, u._name, u.login, u.birthday, u.email from friend_list fl " +
                    "inner join _users u on u.id = fl.friend_id " +
                    "where fl.user_id = ? and fl.friend_request_status = ?";
    private final String GET_USER_LIST = "select * from _users";
    private final String CREATE_USER =
            "INSERT INTO _users(_name, login, email, birthday) " +
                    "VALUES (?, ?, ?, ?)";
    private final String UPDATE_USER_BY_ID = "UPDATE _users SET _name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
    private final String DELETE_USER_BY_ID = "delete from _users where id = ?";
    private final String DELETE_USER_FRIENDS = "delete from friend_list where user_id = ? or friend_id = ?";
    private final String ADD_USER_FRIEND = "insert into friend_list(user_id, friend_id, friend_request_status) values(?, ?, ?)";
    private final String DELETE_FRIEND = "delete from friend_list where user_id = ? and friend_id = ? or user_id = ? and friend_id = ?";

    @Override
    public Optional<User> getUserById(long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(GET_USER_BY_ID, userId);
        if (userRows.next()) {
            return Optional.of(getUserFromRow(userRows, userId));
        }
        return Optional.empty();
    }

    @Override
    public List<User> getUserList() {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(GET_USER_LIST);
        return getUserListFromRowSet(rowSet);
    }

    @Override
    public User addUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_USER,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getEmail());
            statement.setDate(4, java.sql.Date.valueOf(user.getBirthday()));

            return statement;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(UPDATE_USER_BY_ID,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );

        return user;
    }

    @Override
    public void deleteUser(long userId) {
        jdbcTemplate.update(DELETE_USER_FRIENDS, userId);
        jdbcTemplate.update(DELETE_USER_BY_ID, userId);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update(ADD_USER_FRIEND, userId, friendId, "CONFIRMED");
        jdbcTemplate.update(ADD_USER_FRIEND, friendId, userId, "UNCONFIRMED");
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(DELETE_FRIEND,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, userId);
            statement.setLong(2, friendId);
            statement.setLong(3, friendId);
            statement.setLong(4, userId);
            return statement;
        }, keyHolder);

        return keyHolder.getKey() != null;
    }

    @Override
    public List<User> getFriendList(long userId) {
        return getSubList(userId, "CONFIRMED");
    }

    private List<User> getUserListFromRowSet(SqlRowSet rowSet) {
        List<User> userList = new ArrayList<>();

        while (rowSet.next()) {
            userList.add(User.builder()
                    .id(rowSet.getLong("ID"))
                    .name(rowSet.getString("_NAME"))
                    .login(rowSet.getString("login"))
                    .birthday(Objects.requireNonNull(rowSet.getDate("birthday")).toLocalDate())
                    .email(rowSet.getString("email"))
                    .build()
            );
        }
        return userList;
    }

    private User getUserFromRow(SqlRowSet rowSet, long userId) {
        return User.builder()
                .id(rowSet.getLong("ID"))
                .name(rowSet.getString("_NAME"))
                .login(rowSet.getString("login"))
                .birthday(Objects.requireNonNull(rowSet.getDate("birthday")).toLocalDate())
                .email(rowSet.getString("email"))
                .friendLists(getSubList(userId, "CONFIRMED"))
                .friendRequestList(getSubList(userId, "UNCONFIRMED"))
                .build();
    }

    private List<User> getSubList(long userId, String friendRequestStatus) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(GET_USER_FRIENDLIST, userId, friendRequestStatus);

        List<User> userList = new ArrayList<>();
        while (userRows.next()) {

            userList.add(
                    User.builder()
                            .id(userRows.getLong("ID"))
                            .name(userRows.getString("_name"))
                            .login(userRows.getString("LOGIN"))
                            .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                            .email(userRows.getString("email"))
                            .build()
            );
        }
        return userList;
    }
}
