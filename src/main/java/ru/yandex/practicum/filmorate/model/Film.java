package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.enums.MotionPictureAssociationRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {

    private long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private int duration;

    private String genre;

    private MotionPictureAssociationRate tmaRate;

    private List<User> userLikes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration && name.equals(film.name) && Objects.equals(description, film.description) && releaseDate.equals(film.releaseDate) && Objects.equals(genre, film.genre) && tmaRate == film.tmaRate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, releaseDate, duration, genre, tmaRate);
    }
}
