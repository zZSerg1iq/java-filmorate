package ru.yandex.practicum.filmorate.db.dao.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dao.entity.Film;
import ru.yandex.practicum.filmorate.db.dao.entity.Genre;
import ru.yandex.practicum.filmorate.db.dao.entity.MpaRate;
import ru.yandex.practicum.filmorate.db.dao.entity.User;
import ru.yandex.practicum.filmorate.db.dao.repository.FilmRepository;

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

    private final String GET_FILM_BY_ID = "select * from films where id = ?";
    private final String FIND_FILM_BY_DATA =
            "select * from films f " +
                    "where f._name = ? " +
                    "and f.RELEASE_DATE = ?";
    private final String CREATE_FILM =
            "INSERT INTO films(_name, description, release_date, duration) " +
                    "VALUES (?, ?, ?, ?)";
    private final String GET_FILM_LIST = "select * from films";
    private final String UPDATE_FILM_BY_ID = "UPDATE film SET _name = ?, description = ?, release_date = ?, duration = ?, genre = ?, mpa_rate = ? WHERE id = ?";
    private final String DELETE_FILM_BY_ID = "delete from films where id = ?";


    private final String GET_USER_LIKES =
            "select u.id, u._name, u.login, u.birthday, u.email " +
                    "from user_likes ul " +
                    "inner join _users u on u.id = ul.user_id " +
                    "where ul.film_id = ?";
    private final String DELETE_FILM_USER_LIKES = "delete from user_likes where film_id = ?";
    private final String ADD_USER_LIKE = "insert into user_likes(user_id, film_id) values(?, ?)";
    private final String DELETE_USER_LIKE = "delete from user_likes where user_id = ? and film_id = ?";


    private final String CREATE_FILM_MPA_RATE =
            "INSERT INTO film_mpa_rate (film_id, rate_id) " +
                    "VALUES (?, ?)";
    private final String GET_MPA_RATE =
            "select mr.id, mr._rate, mr.description from film_mpa_rate fmr " +
                    "inner join mpa_rate mr on mr.id = fmr.rate_id " +
                    "where film_id = ?";
    private final String UPDATE_FILM_MPA_RATE = "UPDATE film_mpa_rate SET rate_id = ? WHERE film_id = ?";
    private final String DELETE_FILM_MPA_RATE = "delete from film_mpa_rate where film_id = ?";
    private final String SELECT_ALL_MPA = "select * from mpa_rate";
    private final String SELECT_MPA_BY_ID = "select * from mpa_rate where id = ?";


    private final String GET_FILM_GENRES =
            "select g._name from film_genres fg" +
                    " inner join genres g on g.id = fg.genre_id " +
                    " where film_id = ?";
    private final String DELETE_FILM_GENRES = "DELETE FROM film_genres WHERE film_id = ?";
    private final String CREATE_FILM_GENRES = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private final String SELECT_ALL_GENRES = "select * from genres";
    private final String SELECT_GENRE_BY_ID = "select * from genres where id = ?";


    @Override
    public Optional<Film> getFilmById(long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(GET_FILM_BY_ID, filmId);
        if (filmRows.next()) {
            Film film = getFilmFromRow(filmRows);
            return Optional.of(film);
        }
        return Optional.empty();
    }


    @Override
    public List<Film> findFilmByData(Film film) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(FIND_FILM_BY_DATA,
                film.getName(), film.getReleaseDate());

        return getFilmListFromRowSet(rowSet);
    }

    @Override
    public List<Film> getFilmList() {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(GET_FILM_LIST);
        return getFilmListFromRowSet(rowSet);
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_FILM,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            return statement;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        jdbcTemplate.update(CREATE_FILM_MPA_RATE, film.getId(), film.getMpaRate().getId());

        List<Genre> genres = film.getGenres();
        for (Genre genre : genres) {
            jdbcTemplate.update(CREATE_FILM_GENRES, film.getId(), genre.getId());
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(UPDATE_FILM_BY_ID,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpaRate(),
                film.getId());

        jdbcTemplate.update(UPDATE_FILM_MPA_RATE, film.getMpaRate().getId(), film.getId());

        jdbcTemplate.update(DELETE_FILM_GENRES, film.getId());

        // Добавляем новые связи жанров с фильмом
        List<Genre> genres = film.getGenres();
        for (Genre genre : genres) {
            jdbcTemplate.update(CREATE_FILM_GENRES, film.getId(), genre.getId());
        }

        return film;
    }

    @Override
    public void deleteFilm(long id) {
        jdbcTemplate.update(DELETE_FILM_USER_LIKES, id);
        jdbcTemplate.update(DELETE_FILM_BY_ID, id);
        jdbcTemplate.update(DELETE_FILM_MPA_RATE, id);
    }

    @Override
    public boolean addUserLike(long filmId, long userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_USER_LIKE,
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
            PreparedStatement statement = connection.prepareStatement(DELETE_USER_LIKE,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, userId);
            statement.setLong(2, filmId);
            return statement;

        }, keyHolder);

        return keyHolder.getKey() != null;
    }

    @Override
    public List<Film> getTopRate(int count) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(GET_FILM_LIST);

        List<Film> filmList = getFilmListFromRowSet(sqlRowSet);
        filmList.sort(Comparator.comparing(film -> film.getUserLikes().size(), Comparator.reverseOrder()));

        return filmList.stream().limit(count).collect(Collectors.toList());
    }

    @Override
    public List<Genre> getGenres() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(SELECT_ALL_GENRES);
        List<Genre> genres = new ArrayList<>();
        while (sqlRowSet.next()) {
            genres.add(Genre.builder()
                    .id(sqlRowSet.getLong("ID"))
                    .name(sqlRowSet.getString("_NAME"))
                    .build());
        }

        return genres;
    }

    @Override
    public Genre getGenreById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(SELECT_GENRE_BY_ID, id);

        if (sqlRowSet.next()) {
            return Genre.builder()
                    .id(sqlRowSet.getLong("ID"))
                    .name(sqlRowSet.getString("_NAME"))
                    .build();
        }
        return new Genre();
    }

    @Override
    public List<MpaRate> getMpaRateList() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(SELECT_ALL_MPA);
        List<MpaRate> rateList = new ArrayList<>();

        while (sqlRowSet.next()) {
            rateList.add(MpaRate.builder()
                    .id(sqlRowSet.getLong("ID"))
                    .rate(sqlRowSet.getString("_rate"))
                    .description(sqlRowSet.getString("description"))
                    .build());
        }

        return rateList;
    }

    @Override
    public MpaRate getMpaRateById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(SELECT_MPA_BY_ID);

        if (sqlRowSet.next()) {
            return MpaRate.builder()
                    .id(sqlRowSet.getLong("ID"))
                    .rate(sqlRowSet.getString("_rate"))
                    .description(sqlRowSet.getString("description"))
                    .build();
        }

        return new MpaRate();
    }

    private List<Film> getFilmListFromRowSet(SqlRowSet rowSet) {
        //showQueryInfo(rowSet);

        List<Film> filmList = new ArrayList<>();
        while (rowSet.next()) {
            filmList.add(getFilmFromRow(rowSet));
        }

        return filmList;
    }

    private Film getFilmFromRow(SqlRowSet rowSet) {
        long id = rowSet.getLong("ID");

        return Film.builder()
                .id(id)
                .name(rowSet.getString("_NAME"))
                .description(rowSet.getString("DESCRIPTION"))
                .releaseDate(Objects.requireNonNull(rowSet.getDate("RELEASE_DATE")).toLocalDate())
                .duration(rowSet.getInt("DURATION"))
                .mpaRate(getFilmMPARate(id))
                .genres(getGenres(id))
                .userLikes(getUserLikes(id))
                .build();
    }

    private MpaRate getFilmMPARate(long filmId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(GET_MPA_RATE, filmId);
        if (!userRows.next()) {
            return new MpaRate();
        }

        return MpaRate
                .builder()
                .id(userRows.getLong("ID"))
                .rate(userRows.getString("rate"))
                .description(userRows.getString("description"))
                .build();
    }

    private List<Genre> getGenres(long filmId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(GET_FILM_GENRES, filmId);

        List<Genre> userList = new ArrayList<>();
        while (userRows.next()) {
            userList.add(
                    Genre.builder()
                            .id(userRows.getLong("ID"))
                            .name(userRows.getString("_NAME"))
                            .build()
            );
        }
        return userList;
    }

    private List<User> getUserLikes(long filmId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(GET_USER_LIKES, filmId);

        List<User> userList = new ArrayList<>();
        while (userRows.next()) {

            userList.add(
                    User.builder()
                            .id(userRows.getLong("ID"))
                            .name(userRows.getString("_NAME"))
                            .login(userRows.getString("LOGIN"))
                            .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
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
