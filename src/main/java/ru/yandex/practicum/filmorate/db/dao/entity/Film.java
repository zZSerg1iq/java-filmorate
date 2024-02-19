package ru.yandex.practicum.filmorate.db.dao.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Film {

    private long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private int duration;

    private MpaRate mpa;

    private List<Genre> genres;

    private List<User> userLikes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(mpa, film.mpa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, releaseDate, duration, mpa);
    }
}
