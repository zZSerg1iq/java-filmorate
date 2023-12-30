package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validation.DateTimeMin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
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

    private Set<Long> userLikes;

    public boolean addUserLike(long userId) {
        return userLikes.add(userId);
    }

    public boolean deleteUserLike(long userId) {
        return userLikes.remove(userId);
    }

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Set<Long> userLikes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.userLikes = userLikes;

        if (userLikes == null) {
            this.userLikes = new HashSet<>();
        }
    }

    public Film() {
        this.userLikes = new HashSet<>();
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
