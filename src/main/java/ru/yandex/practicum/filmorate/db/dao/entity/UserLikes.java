package ru.yandex.practicum.filmorate.db.dao.entity;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserLikes {

    private long id;

    private User user;

    private Film film;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLikes userLikes = (UserLikes) o;
        return Objects.equals(user, userLikes.user) && Objects.equals(film, userLikes.film);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, film);
    }
}
