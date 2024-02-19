package ru.yandex.practicum.filmorate.db.dao.entity;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private long id;

    private String name;

    private String login;

    private String email;

    private Date birthday;

    private List<User> friendLists;

    private List<User> friendRequestList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && login.equals(user.login) && email.equals(user.email) && Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, login, email, birthday);
    }
}
