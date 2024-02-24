package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.db.dto.entity.FilmDto;
import ru.yandex.practicum.filmorate.db.dto.entity.GenreDto;
import ru.yandex.practicum.filmorate.db.dto.entity.MpaRateDto;
import ru.yandex.practicum.filmorate.db.dto.entity.UserDto;
import ru.yandex.practicum.filmorate.db.dto.servise.FilmRepositoryService;
import ru.yandex.practicum.filmorate.db.dto.servise.UserRepositoryService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")
@SpringBootTest
public abstract class TestBasic {

    protected final String dropAllQuery =
            "DROP TABLE IF EXISTS _users cascade ; " +
                    "DROP TABLE IF EXISTS films cascade ;" +
                    "DROP TABLE IF EXISTS friend_list cascade ;" +
                    "DROP TABLE IF EXISTS user_likes cascade ;" +
                    "DROP TABLE IF EXISTS genres cascade ;" +
                    "DROP TABLE IF EXISTS film_genres cascade ;" +
                    "DROP TABLE IF EXISTS mpa_rate cascade ;" +
                    "DROP TABLE IF EXISTS film_mpa_rate cascade ;";


    protected final String CREATE_ALL_TABLES_QUERY = "" +
            "CREATE TABLE IF NOT EXISTS _users (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "_name VARCHAR(50) NOT NULL," +
            "login VARCHAR(50) NOT NULL," +
            "email VARCHAR(50) NOT NULL," +
            "birthday Date" +
            ");" +
            "" +
            "CREATE TABLE IF NOT EXISTS films (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "_name VARCHAR(50)," +
            "description VARCHAR(200)," +
            "release_date DATE," +
            "duration INT" +
            ");" +
            "" +
            "CREATE TABLE IF NOT EXISTS friend_list (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "user_id INT," +
            "friend_id INT," +
            "friend_request_status VARCHAR(20)," +
            "FOREIGN KEY (user_id) REFERENCES _users(id)," +
            "FOREIGN KEY (friend_id) REFERENCES _users(id)" +
            ");" +
            "" +
            "CREATE TABLE IF NOT EXISTS user_likes (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "user_id INT," +
            "film_id INT," +
            "FOREIGN KEY (user_id) REFERENCES _users(id)," +
            "FOREIGN KEY (film_id) REFERENCES films(id)" +
            ");" +
            "" +
            "create table if not exists genres (" +
            "id int auto_increment primary key," +
            "_name varchar(50)" +
            ");" +
            "" +
            "CREATE TABLE IF NOT EXISTS film_genres (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "film_id INT," +
            "genre_id INT," +
            "FOREIGN KEY (film_id) REFERENCES films(id)," +
            "FOREIGN KEY (genre_id) REFERENCES genres(id)" +
            ");" +
            "" +
            "create table if not exists mpa_rate (" +
            "id int auto_increment primary key," +
            "_rate varchar(10)," +
            "description varchar(100)" +
            ");" +
            "" +
            "CREATE TABLE IF NOT EXISTS film_mpa_rate (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "film_id INT," +
            "rate_id INT," +
            "FOREIGN KEY (film_id) REFERENCES films(id)," +
            "FOREIGN KEY (rate_id) REFERENCES mpa_rate(id)" +
            ");";

    protected final String insert_started_data = "" +
            "INSERT INTO genres (_name) values " +
            "('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик'); " +
            "" +
            "INSERT INTO mpa_rate (_rate, description)  values " +
            "('G', 'у фильма нет возрастных ограничений'),  " +
            "('PG', 'детям рекомендуется смотреть фильм с родителями'), " +
            "('PG-13', 'детям до 13 лет просмотр не желателен'), " +
            "('R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого')," +
            "('NC-17', 'лицам до 18 лет просмотр запрещён');";


    @Autowired
    protected UserRepositoryService userService;

    @Autowired
    protected FilmRepositoryService filmService;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected Random random = new Random();


    @BeforeEach
    public void setUp() {
        jdbcTemplate.update(dropAllQuery);
        jdbcTemplate.update(CREATE_ALL_TABLES_QUERY);
        jdbcTemplate.update(insert_started_data);

        Assertions.assertAll(
                () -> assertEquals(0, filmService.getFilmList().size()),
                () -> assertEquals(0, userService.getUserList().size())
        );
    }


    protected FilmDto getRandomFilmDto() {
        return FilmDto
                .builder()
                .name(getRandomStr())
                .description(getRandomStr())
                .duration(random.nextInt(300) + 2)
                .releaseDate(getRandomDate(1980, 2023))
                .mpa(getMpaDto(10))
                .genres(getGenres(1))
                .build();
    }

    protected MpaRateDto getMpaDto(int rateId) {
        if (rateId < 1 || rateId > 5) {
            rateId = random.nextInt(4) + 1;
        }
        return MpaRateDto.builder().id(rateId).build();
    }

    protected UserDto getRandomUserDto() {
        return UserDto
                .builder()
                .name(getRandomStr())
                .login(getRandomStr())
                .birthday(getRandomDate(1900, 2023))
                .email(getRandomEmail())
                .build();
    }

    protected List<GenreDto> getGenres(int count) {
        return IntStream.range(1, 6)
                .limit(count)
                .mapToObj(operand -> GenreDto.builder().id(operand).build())
                .collect(Collectors.toList());
    }

    private String getRandomStr() {
        String temp = UUID.randomUUID().toString();
        return temp.substring(0, random.nextInt(31) + 5);
    }

    private LocalDate getRandomDate(int minYear, int maxYear) {
        Random random = new Random();
        int minDay = (int) LocalDate.of(minYear, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(maxYear, 12, 31).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public String getRandomEmail() {
        String uuid = UUID.randomUUID().toString(); // Генерация случайного UUID
        return "userMail" + uuid.substring(0, 8) + "@example.com"; // Собираем e-mail из UUID
    }

    private LocalDate getDateFromString(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }

}
