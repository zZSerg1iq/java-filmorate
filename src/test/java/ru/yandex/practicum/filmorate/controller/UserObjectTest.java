package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MyUserDataStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserObjectTest {

    private static Validator validator;

    @BeforeAll
    private static void prepare() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void correctUserObjectTest() {
        MyUserDataStorage myUserDataStorage = new MyUserDataStorage();

        User user = new User();
        user.setName("user");
        user.setLogin("login");
        user.setEmail("mail@mymail.com");
        user.setBirthday(LocalDate.parse("1990-10-10"));

        //валидный объект
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(0, constraintViolations.size());

        //отсутствуюшее имя
        user.setName(null);
        constraintViolations = validator.validate(user);
        assertEquals(0, constraintViolations.size());
        User result = myUserDataStorage.addUser(user);
        assertEquals(result.getName(), result.getLogin());


        //отсутсвующий логин
        user.setName("name");
        user.setLogin(null);
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<User> violationError = constraintViolations.iterator().next();
        assertEquals("NotBlank", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());


        //некорректный email
        user.setLogin("login");
        user.setEmail("mail@mymail");
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        violationError = constraintViolations.iterator().next();
        assertEquals("Email", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //некорректный email 2
        user.setEmail(null);
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        violationError = constraintViolations.iterator().next();
        assertEquals("NotNull", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //некорректный email 3
        user.setEmail("@h.e");
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        violationError = constraintViolations.iterator().next();
        assertEquals("Email", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //некорректный email 4
        user.setEmail("lala.ru");
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        violationError = constraintViolations.iterator().next();
        assertEquals("Email", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //некорректный email 5
        user.setEmail("");
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        violationError = constraintViolations.iterator().next();
        assertEquals("Email", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //некорректная дата рождения
        user.setEmail("mail@mymail.com");
        user.setBirthday(LocalDate.now());
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        violationError = constraintViolations.iterator().next();
        assertEquals("Past", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //некорректная дата рождения
        user.setBirthday(LocalDate.now().plusYears(10));
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        violationError = constraintViolations.iterator().next();
        assertEquals("Past", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());
    }
}
