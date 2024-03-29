package ru.yandex.practicum.filmorate.db.dao.entity;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Genre {

    private long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genreDto = (Genre) o;
        return Objects.equals(name, genreDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
