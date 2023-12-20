package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final String URL = "http://localhost:8080/users";

    @Test
    void getUser() {
        LocalDate date = LocalDate.of(2000, 2, 2);
        User user = new User(0, "Аквамен", "aquamenBoYYYYY", "mail@mail.mail", date);
        Assertions.assertEquals(HttpStatus.CREATED, sendRequest(HttpMethod.POST, user));

        //получаю список у сервиса
        List<User> returned = sendGetList();
        assertNotNull(returned);

        // получаю пользователя из списка
        User userFromList = returned.stream()
                .filter(u -> u.getName().equals("Аквамен"))
                .findFirst().orElse(null);
        assertNotNull(userFromList);

        //получаю пользователя по id у сервиса
        User user1 = sendGetOne(userFromList.getId());
        assertNotNull(user1);
        assertEquals(user1, userFromList);

        // пробую получить несуществующего пользователя
        Executable executable = () -> sendGetOne(3244);
        assertThrows(HttpClientErrorException.NotFound.class, executable);
    }

    @Test
    void getUserList() {
        LocalDate date = LocalDate.of(1995, 2, 2);
        User user = new User(0, "UserUser", "UserLogin", "Email@email.email", date );
        Assertions.assertEquals(HttpStatus.CREATED, sendRequest(HttpMethod.POST, user));

        List<User> returned = sendGetList();
        assertNotNull(returned);
        assert returned.size() >= 1;
    }

    @Test
    void addUser() {
        // создаю и добавляю пользователя
        LocalDate date = LocalDate.of(1999, 2, 2);
        User user = new User(0, "UserUser", "UserLogin", "Email@email.email", date );
        Assertions.assertEquals(HttpStatus.CREATED, sendRequest(HttpMethod.POST, user));


        // добавляю копию пользователя (должно добавиться, потому что полные тезки существуют)
        Assertions.assertEquals(HttpStatus.CREATED, sendRequest(HttpMethod.POST, user));


        // добавляю пользователя с пустым именем и проверяю что вместо имени стал логин
        date = LocalDate.of(1980, 2, 2);
        User noNameUser = new User(0, "", "NameLogin_1", "Email@email.email", date);
        Assertions.assertEquals(HttpStatus.CREATED, sendRequest(HttpMethod.POST, noNameUser));

        // получаю список и проверяю
        List<User> users = sendGetList();
        User noNameUserReturned = users.stream()
                .filter(user1 -> user1.getLogin().equals("NameLogin_1"))
                .findFirst()
                .orElse(null);
        assertNotNull(noNameUserReturned);
        assertEquals("NameLogin_1", noNameUserReturned.getName());




        // пользователь, с пустым логином
        date = LocalDate.of(1981, 2, 2);
        User wrongLoginUser = new User(0, "UserUser", null, "Email@email.email", date );
        Executable executable2_1 = () -> sendRequest(HttpMethod.POST, wrongLoginUser);
        assertThrows(HttpClientErrorException.BadRequest.class, executable2_1);
        User wrongLoginUser2 = new User(0, "UserUser", "", "Email@email.email", date );
        Executable executable2_2 = () -> sendRequest(HttpMethod.POST, wrongLoginUser2);
        assertThrows(HttpClientErrorException.BadRequest.class, executable2_2);




        // некорректный емейл
        date = LocalDate.of(2099, 2, 2);
        User wrongEmail = new User(0, "UserUser1", "UserLogin1", "Email.email", date );
        Executable executable3 = () -> sendRequest(HttpMethod.POST, wrongEmail);
        assertThrows(HttpClientErrorException.BadRequest.class, executable3);

        User wrongEmail2 = new User(0, "UserUser1", "UserLogin1", "Email@", date );
        Executable executable3_1 = () -> sendRequest(HttpMethod.POST, wrongEmail2);
        assertThrows(HttpClientErrorException.BadRequest.class, executable3_1);

        User wrongEmail3 = new User(0, "UserUser1", "UserLogin1", "@g.r", date );
        Executable executable3_2 = () -> sendRequest(HttpMethod.POST, wrongEmail3);
        assertThrows(HttpClientErrorException.BadRequest.class, executable3_2);

        User wrongEmail4 = new User(0, "UserUser1", "UserLogin1", "", date );
        Executable executable3_3 = () -> sendRequest(HttpMethod.POST, wrongEmail4);
        assertThrows(HttpClientErrorException.BadRequest.class, executable3_3);


        // некорректная дата рождения
        date = LocalDate.of(2025, 2, 2);
        User incorrectBirth = new User(0, "UserUser", "UserLogin", "Email@email.email", date );
        Executable executable4 = () -> sendRequest(HttpMethod.POST, incorrectBirth);
        assertThrows(HttpClientErrorException.BadRequest.class, executable4);
    }

    @Test
    void updateUser() {
        // добавление пользователя для последующего изменения (в случае, если тест будет запущен первым)
        LocalDate date = LocalDate.of(2001, 7, 2);
        User User = new User(0, "UserUser", "UserLogin", "Email@email.email", date );
        Assertions.assertEquals(HttpStatus.CREATED, sendRequest(HttpMethod.POST, User));

        // получение списка и пользователя
        List<User> returned = sendGetList();
        assertNotNull(returned);
        User returnedUser = returned.get(0);
        long id = returnedUser.getId();

        // изменение данных и отправка
        returnedUser.setName("");
        returnedUser.setLogin("TestLogin_yeah_booyyyy");
        Assertions.assertEquals(HttpStatus.OK, sendRequest(HttpMethod.PUT, returnedUser));

        // получение отредактированного юзера и сравнение
        User returnedUser2 = sendGetOne(id);
        assertEquals("TestLogin_yeah_booyyyy", returnedUser2.getName());
        assertEquals("TestLogin_yeah_booyyyy", returnedUser2.getLogin());
    }

    @Test
    void deleteUser() {
    }


    private HttpStatus sendRequest(HttpMethod method, User User) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(User, headers);

        return new RestTemplate().exchange(
                URL,
                method,
                entity,
                User.class
        ).getStatusCode();
    }

    private List<User> sendGetList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<User>> response = new RestTemplate().exchange(
                URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }

    private User sendGetOne(long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        String url = URL + "/" + id;

        ResponseEntity<User> response = new RestTemplate().exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }
}