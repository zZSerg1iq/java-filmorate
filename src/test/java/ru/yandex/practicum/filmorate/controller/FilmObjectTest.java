package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.dto.servise.FilmRepositoryService;
import ru.yandex.practicum.filmorate.db.dto.servise.UserRepositoryService;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


@SpringBootTest
class FilmObjectTest {

    @Autowired
    private UserRepositoryService userStorageService;

    @Autowired
    private FilmRepositoryService filmStorageService;

    private static Validator validator;

    @BeforeAll
    public static void prepare() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

   /* @Test
    public void correctFilmObjectTest() {
        Film film = new Film();
        film.setId(1);
        film.setName("Film Name");
        film.setDescription("Film Description with less than 200 characters");
        film.setReleaseDate(getDateFromString("2022-01-01"));
        film.setDuration(120);

        //Корректный объект
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertEquals(0, violations.size());
    }


    @Test
    public void filmDurationTest() {
        Film film = new Film();

        //длительность = 0
        film.setName("Film Name");
        film.setDescription("Film Description with less than 200 characters");
        film.setReleaseDate(getDateFromString("2022-01-01"));
        film.setDuration(0);
        Set<ConstraintViolation<Film>> durationViolations = validator.validate(film);
        Assertions.assertEquals(1, durationViolations.size());
        ConstraintViolation<Film> violation = durationViolations.iterator().next();
        Assertions.assertEquals("Positive", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //длительность = -1
        film.setDuration(-1);
        durationViolations = validator.validate(film);
        Assertions.assertEquals(1, durationViolations.size());
        violation = durationViolations.iterator().next();
        Assertions.assertEquals("Positive", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //длительность = null
        film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description with less than 200 characters");
        film.setReleaseDate(getDateFromString("2022-01-01"));
        durationViolations = validator.validate(film);
        Assertions.assertEquals(1, durationViolations.size());
        violation = durationViolations.iterator().next();
        Assertions.assertEquals("Positive", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }

    @Test
    public void filmNameTest() {

        Film film = new Film();
        film.setDescription("Film Description with less than 200 characters");
        film.setReleaseDate(getDateFromString("2022-01-01"));
        film.setDuration(10);

        //без названия
        film.setName(null);
        Set<ConstraintViolation<Film>> durationViolations = validator.validate(film);
        Assertions.assertEquals(1, durationViolations.size());
        ConstraintViolation<Film> violation = durationViolations.iterator().next();
        Assertions.assertEquals("NotBlank", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //пустое название
        film.setName(" ");
        durationViolations = validator.validate(film);
        Assertions.assertEquals(1, durationViolations.size());
        violation = durationViolations.iterator().next();
        Assertions.assertEquals("NotBlank", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }

    @Test
    public void filmDescriptionLengthTest() {
        Film film = new Film();
        film.setName("Film name");
        film.setReleaseDate(getDateFromString("2022-01-01"));
        film.setDuration(10);

        //без описания
        film.setDescription(null);
        Set<ConstraintViolation<Film>> descriptionViolations = validator.validate(film);
        Assertions.assertEquals(0, descriptionViolations.size());

        //Слишком длинное описание
        String description = "Какое-то длинное описание фильма";
        film.setDescription(description.repeat(100));
        descriptionViolations = validator.validate(film);
        Assertions.assertEquals(1, descriptionViolations.size());
        ConstraintViolation<Film> violation = descriptionViolations.iterator().next();
        Assertions.assertEquals("Size", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }

    @Test
    public void filmDateTest() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Some description");
        film.setDuration(10);

        //минимальная дата
        film.setReleaseDate(getDateFromString("1895-12-28"));
        Set<ConstraintViolation<Film>> descriptionViolations = validator.validate(film);
        Assertions.assertEquals(0, descriptionViolations.size());

        //слишком ранняя дата
        film.setReleaseDate(getDateFromString("1890-01-01"));
        descriptionViolations = validator.validate(film);
        Assertions.assertEquals(1, descriptionViolations.size());
        ConstraintViolation<Film> violation = descriptionViolations.iterator().next();
        Assertions.assertEquals("DateTimeMin", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //отсутствующая дата
        film.setReleaseDate(null);
        descriptionViolations = validator.validate(film);
        Assertions.assertEquals(1, descriptionViolations.size());
        violation = descriptionViolations.iterator().next();
        Assertions.assertEquals("DateTimeMin", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //Корректная дата
        film.setReleaseDate(getDateFromString("1990-01-01"));
        descriptionViolations = validator.validate(film);
        Assertions.assertEquals(0, descriptionViolations.size());
    }

    @Test
    public void filmUserLikeTest() {
        //создаем и добавляем 2 пользователей и фильм
        User user1 = new User();
        user1.setName("user");
        user1.setLogin("login");
        user1.setEmail("mail@mymail.com");
        user1.setBirthday(getDateFromString("1990-10-10"));

        User user2 = new User();
        user2.setName("user2");
        user2.setLogin("login2");
        user2.setEmail("mail@mymail2.com");
        user2.setBirthday(getDateFromString("1991-10-10"));

        long user1Id = userStorageService.addUser(user1).getId();
        long user2Id = userStorageService.addUser(user2).getId();


        Film film = new Film();
        film.setName("Film name");
        film.setReleaseDate(getDateFromString("2022-01-01"));
        film.setDuration(10);
        long filmId = filmStorageService.addFilm(film).getId();


        //удаляем лайк к несуществующему фильму
        Executable executable = () -> filmStorageService.addUserLike(123456, user1Id);
        Assertions.assertThrows(DataNotFoundException.class, executable);

        //добавляем лайк от первого пользователя
        filmStorageService.addUserLike(filmId, user1Id);
        Assertions.assertEquals(1, film.getUserLikes().size());

        //добавляем его еще раз
        executable = () -> filmStorageService.addUserLike(filmId, user1Id);
        Assertions.assertThrows(DataConflictException.class, executable);

        //добавляем лайк от второго пользователя
        filmStorageService.addUserLike(filmId, user2Id);
        Assertions.assertEquals(2, film.getUserLikes().size());

        //удаляем лайк
        filmStorageService.deleteUserLike(filmId, user1Id);
        Assertions.assertEquals(1, film.getUserLikes().size());

        //удаляем его же еще раз
        executable = () -> filmStorageService.deleteUserLike(filmId, user1Id);
        Assertions.assertThrows(DataNotFoundException.class, executable);

        //удаляем второй лайк
        filmStorageService.deleteUserLike(filmId, user2Id);
        Assertions.assertEquals(0, film.getUserLikes().size());

        //удаляем лайк у не существующего фильма
        executable = () -> filmStorageService.deleteUserLike(123456, user1Id);
        Assertions.assertThrows(DataNotFoundException.class, executable);

    }


    @Test
    public void filmTopRateTest() {
        //создаем и добавляем 10 пользователей и 15 фильмов
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = userStorageService.addUser(
                    new User(0, "user_" + i, "login_" + i, "mail" + i + "@mymail.com", getDateFromString("1990-10-10"), null)
            );
            userList.add(user);
        }

        List<Film> films = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Film film = filmStorageService.addFilm(
                    new Film(0, "Film name_" + i, "description_" + i, getDateFromString("2022-01-01"), 10, null)
            );
            films.add(film);
        }


        //добавляем пяти фильмам по лайку
        for (int i = 0; i < 5; i++) {
            filmStorageService.addUserLike(films.get(i).getId(), userList.get(i).getId());
        }
        List<Film> topList = filmStorageService.getTopRate(5);
        Assertions.assertEquals(5, topList.size());

        //check likes count
        int likesCount = 0;
        for (Film film : topList) {
            likesCount += film.getUserLikes().size();
        }
        Assertions.assertEquals(5, likesCount);


        //add a few likes to films
        for (int i = 0; i < 9; i++) {
            filmStorageService.addUserLike(films.get(i).getId(), userList.get(i + 1).getId());
        }
        topList = filmStorageService.getTopRate(5);
        Assertions.assertEquals(5, topList.size());

        likesCount = 0;
        for (Film film : topList) {
            likesCount += film.getUserLikes().size();
        }
        Assertions.assertEquals(10, likesCount);


        //all films likes count = 5 + 9
        topList = filmStorageService.getTopRate(100);
        likesCount = 0;
        for (Film film : topList) {
            likesCount += film.getUserLikes().size();
        }
        Assertions.assertEquals(14, likesCount);


        //get first film and take it 5 likes
        Film film = films.get(0);

        int userId = 10;
        filmStorageService.addUserLike(film.getId(), userId--);
        filmStorageService.addUserLike(film.getId(), userId--);
        filmStorageService.addUserLike(film.getId(), userId--);
        filmStorageService.addUserLike(film.getId(), userId--);
        filmStorageService.addUserLike(film.getId(), userId);

        topList = filmStorageService.getTopRate(10);
        Film topFilm = topList.get(0);
        Assertions.assertEquals(film, topFilm);
        Assertions.assertEquals(7, topFilm.getUserLikes().size());


        //remove likes from top film
        filmStorageService.deleteUserLike(topFilm.getId(), userId++);
        filmStorageService.deleteUserLike(topFilm.getId(), userId++);
        filmStorageService.deleteUserLike(topFilm.getId(), userId++);
        filmStorageService.deleteUserLike(topFilm.getId(), userId++);
        filmStorageService.deleteUserLike(topFilm.getId(), userId);

        topList = filmStorageService.getTopRate(10);
        film = topList.get(0);
        Assertions.assertEquals(2, film.getUserLikes().size());


        //check likes count = 14 + 5 - 5
        likesCount = 0;
        for (Film f : topList) {
            likesCount += f.getUserLikes().size();
        }
        Assertions.assertEquals(14, likesCount);
    }
    
    
    private Date getDateFromString(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateStr); // Преобразование строки в объект типа Date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }*/

}
