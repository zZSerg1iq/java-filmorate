package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id = -1;

    private String name;

    @NotBlank
    private String login;

    @Email
    private String email;


    @NonNull
    @Past
    private LocalDate birthday;

}
