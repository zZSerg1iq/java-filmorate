package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;


class FilmObjectTest {

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

}
