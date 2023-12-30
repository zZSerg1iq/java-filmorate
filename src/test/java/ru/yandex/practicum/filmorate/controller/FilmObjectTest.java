package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.DataConflictException;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servise.FilmStorageService;
import ru.yandex.practicum.filmorate.servise.UserStorageService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@SpringBootTest
class FilmObjectTest {

    @Autowired
    private UserStorageService userStorageService;

    @Autowired
    private FilmStorageService filmStorageService;

    private static Validator validator;

    @BeforeAll
    public static void prepare() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void correctFilmObjectTest() {
        Film film = new Film();
        film.setId(1);
        film.setName("Film Name");
        film.setDescription("Film Description with less than 200 characters");
        film.setReleaseDate(LocalDate.parse("2022-01-01"));
        film.setUserLikes(new HashSet<>());
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
        film.setReleaseDate(LocalDate.parse("2022-01-01"));
        film.setDuration(0);
        film.setUserLikes(new HashSet<>());
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
        film.setReleaseDate(LocalDate.parse("2022-01-01"));
        film.setUserLikes(new HashSet<>());
        durationViolations = validator.validate(film);
        Assertions.assertEquals(1, durationViolations.size());
        violation = durationViolations.iterator().next();
        Assertions.assertEquals("Positive", violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }

    @Test
    public void filmNameTest() {
        Film film = new Film();
        film.setDescription("Film Description with less than 200 characters");
        film.setReleaseDate(LocalDate.parse("2022-01-01"));
        film.setDuration(10);
        film.setUserLikes(new HashSet<>());

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
        film.setReleaseDate(LocalDate.parse("2022-01-01"));
        film.setDuration(10);
        film.setUserLikes(new HashSet<>());

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
        film.setUserLikes(new HashSet<>());

        //минимальная дата
        film.setReleaseDate(LocalDate.parse("1895-12-28"));
        Set<ConstraintViolation<Film>> descriptionViolations = validator.validate(film);
        Assertions.assertEquals(0, descriptionViolations.size());

        //слишком ранняя дата
        film.setReleaseDate(LocalDate.parse("1890-01-01"));
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
        film.setReleaseDate(LocalDate.parse("1990-01-01"));
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
        user1.setFriendIdList(new HashSet<>());
        user1.setBirthday(LocalDate.parse("1990-10-10"));

        User user2 = new User();
        user2.setName("user2");
        user2.setLogin("login2");
        user2.setEmail("mail@mymail2.com");
        user2.setFriendIdList(new HashSet<>());
        user2.setBirthday(LocalDate.parse("1991-10-10"));

        long user1Id = userStorageService.addUser(user1).getId();
        long user2Id = userStorageService.addUser(user2).getId();


        Film film = new Film();
        film.setName("Film name");
        film.setReleaseDate(LocalDate.parse("2022-01-01"));
        film.setDuration(10);
        film.setUserLikes(new HashSet<>());
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

}
