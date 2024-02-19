package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.dto.servise.UserRepositoryService;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class UserObjectTest {

    @Autowired
    private UserRepositoryService userStorageService;

    private static Validator validator;

    @BeforeAll
    private static void prepare() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

   /* @Test
    public void correctUserObjectTest() {
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
        User result = userStorageService.addUser(user);
        assertEquals(result.getLogin(), result.getName());


        //нормальный логин
        user.setName("name");
        user.setLogin("login");
        constraintViolations = validator.validate(user);
        assertEquals(0, constraintViolations.size());


        //отсутсвующий логин
        user.setLogin(null);
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        ConstraintViolation<User> violationError = constraintViolations.iterator().next();
        assertEquals("NotNull", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());

        //логин из пробелов
        user.setLogin("     ");
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        violationError = constraintViolations.iterator().next();
        assertEquals("Pattern", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());


        //логин с пробелами
        user.setLogin("12     12");
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        violationError = constraintViolations.iterator().next();
        assertEquals("Pattern", violationError.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName());


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

    @Test
    public void userFriendListTest(){

    }*/
}
