package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class User {

    private long id = -1;

    private String name;

    @NotNull
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private String login;

    @NotNull
    @Email(message = "Email недействительный", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @Past
    private LocalDate birthday;

    private Set<Long> friendIdList;

    public boolean addFriend(long userId) {
        return friendIdList.add(userId);
    }

    public boolean deleteFriend(long userId) {
        return friendIdList.remove(userId);
    }

    public User(long id, String name, String login, String email, LocalDate birthday, Set<Long> friendIdList) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.birthday = birthday;
        this.friendIdList = friendIdList;
        if (friendIdList == null) {
            this.friendIdList = new HashSet<>();
        }
    }

    public User() {
        this.friendIdList = new HashSet<>();
    }
}
