package ru.yandex.practicum.filmorate.db.dao.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.entity.Film;
import ru.yandex.practicum.filmorate.db.dao.entity.User;
import ru.yandex.practicum.filmorate.db.dao.repository.FilmRepository;
import ru.yandex.practicum.filmorate.db.enums.MotionPictureAssociationRate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class H2FilmRepository implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2FilmRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String getFilmByIdQuery = "select * from films where id = ?";

    private final String getUserLikesQuery =
            "select u.id, u._name, u.login, u.birthday, u.email " +
                    "from user_likes ul " +
                    "inner join _users u on u.id = ul.user_id " +
                    "where ul.film_id = ?";

    private final String findFilmByDataQuery =
            "select * from films f " +
                    "where f._name like CONCAT('%', ? '%') " +
                    "and f.RELEASE_DATE = ?";

    private final String getFilmListQuery = "select * from films";

    private final String createFilmQuery =
            "INSERT INTO films(_name, description, release_date, duration, genre, mpa_rate) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private final String updateFilmByIdQuery = "UPDATE film SET _name = ?, description = ?, release_date = ?, duration = ?, genre = ?, mpa_rate = ? WHERE id = ?";
    private final String deleteFilmByIdQuery = "delete from films where id = ?";
    private final String deleteFilmUserLikes = "delete from user_likes where film_id = ?";

    private final String addUserLikeQuery = "insert into user_likes(user_id, film_id) values(?, ?)";
    private final String deleteUserLikeQuery = "delete from user_likes where user_id = ? and film_id = ?";

    @Override
    public Optional<Film> getFilmById(long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(getFilmByIdQuery, filmId);
        if (filmRows.next()) {
            Film film = getFilmFromRow(filmRows);
            return Optional.of(film);
        }
        return Optional.empty();
    }


    @Override
    public List<Film> findFilmByData(Film film) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(findFilmByDataQuery,
                film.getName(), film.getReleaseDate());

        return getFilmListFromRowSet(rowSet);
    }

    @Override
    public List<Film> getFilmList() {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getFilmListQuery);
        return getFilmListFromRowSet(rowSet);
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(createFilmQuery,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, new java.sql.Date(film.getReleaseDate().getTime()));
            statement.setInt(4, film.getDuration());
            statement.setString(5, film.getGenre());
            statement.setString(6, film.getMpaRate().toString());

            return statement;

        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(updateFilmByIdQuery,
                film.getName(),
                film.getDescription(),
                new java.sql.Date(film.getReleaseDate().getTime()),
                film.getDuration(),
                film.getGenre(),
                film.getMpaRate(),
                film.getId());

        return film;
    }

    @Override
    public void deleteFilm(long id) {
        jdbcTemplate.update(deleteFilmUserLikes, id);
        jdbcTemplate.update(deleteFilmByIdQuery, id);
    }

    @Override
    public boolean addUserLike(long filmId, long userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(addUserLikeQuery,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, userId);
            statement.setLong(2, filmId);
            return statement;

        }, keyHolder);

        return keyHolder.getKey() != null;
    }

    @Override
    public boolean deleteUserLike(long filmId, long userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(deleteUserLikeQuery,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, userId);
            statement.setLong(2, filmId);
            return statement;

        }, keyHolder);

        return keyHolder.getKey() != null;
    }

    @Override
    public List<Film> getTopRate(int count) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getFilmListQuery);

        List<Film> filmList = getFilmListFromRowSet(sqlRowSet);
        filmList.sort(Comparator.comparing(film -> film.getUserLikes().size(), Comparator.reverseOrder()));

        return filmList.stream().limit(count).collect(Collectors.toList());
    }


    private List<Film> getFilmListFromRowSet(SqlRowSet rowSet) {
        //showQueryInfo(rowSet);

        List<Film> filmList = new ArrayList<>();
        while (rowSet.next()) {
            filmList.add(
                    Film.builder()
                            .id(rowSet.getLong("ID"))
                            .name(rowSet.getString("_NAME"))
                            .description(rowSet.getString("DESCRIPTION"))
                            .releaseDate(rowSet.getDate("RELEASE_DATE"))
                            .duration(rowSet.getInt("DURATION"))
                            .genre(rowSet.getString("GENRE"))
                            //.mpaRate(MotionPictureAssociationRate.valueOf(rowSet.getString("MPA_RATE")))
                            .userLikes(getUserLikes(rowSet.getLong("ID")))
                            .build()
            );
        }

        return filmList;
    }

    private Film getFilmFromRow(SqlRowSet rowSet) {
        return Film.builder()
                .id(rowSet.getLong("ID"))
                .name(rowSet.getString("_NAME"))
                .description(rowSet.getString("DESCRIPTION"))
                .releaseDate(rowSet.getDate("RELEASE_DATE"))
                .duration(rowSet.getInt("DURATION"))
                .genre(rowSet.getString("GENRE"))
                .mpaRate(MotionPictureAssociationRate.valueOf(rowSet.getString("MPA_RATE")))
                .userLikes(getUserLikes(rowSet.getLong("ID")))
                .build();
    }

    private List<User> getUserLikes(long filmId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(getUserLikesQuery, filmId);

        List<User> userList = new ArrayList<>();
        while (userRows.next()) {

            userList.add(
                    User.builder()
                            .id(userRows.getLong("ID"))
                            .name(userRows.getString("_NAME"))
                            .login(userRows.getString("LOGIN"))
                            .birthday(userRows.getDate("birthday"))
                            .email(userRows.getString("email"))
                            .build()
            );
        }
        return userList;
    }

    /**
     * Метод для вывода данных о результате запроса.
     * В логике не участвует.
     * Нужен для отладки
     */
    private void showQueryInfo(SqlRowSet rows) {
        SqlRowSetMetaData metaData = rows.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String columnLabel = metaData.getColumnLabel(i);
            int columnType = metaData.getColumnType(i);

            System.out.println("Column name: " + columnName);
            System.out.println("Column label: " + columnLabel);
            System.out.println("Column type: " + columnType);
        }
    }
}
