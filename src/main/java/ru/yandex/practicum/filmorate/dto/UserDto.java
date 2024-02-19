package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.enums.FriendRequestStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
public class UserDto {

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

    private List<UserDto> friendList = new ArrayList<>();

    private List<UserDto> friendRequestList = new ArrayList<>();

}
