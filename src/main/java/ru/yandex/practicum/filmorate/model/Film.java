package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validation.DateTimeMin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private long id = -1;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateTimeMin(date = "1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @NotNull
    private Set<Long> userLikes;

    public boolean addUserLike(long userId) {
        return userLikes.add(userId);
    }

    public boolean deleteUserLike(long userId) {
        return userLikes.remove(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration && name.equals(film.name) && Objects.equals(description, film.description) && releaseDate.equals(film.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, releaseDate, duration);
    }
}
