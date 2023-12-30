package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @NotNull
    private Set<Long> friendIdList;

    public boolean addFriend(long userId) {
        return friendIdList.add(userId);
    }

    public boolean deleteFriend(long userId) {
        return friendIdList.remove(userId);
    }

}
