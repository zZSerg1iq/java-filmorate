package ru.yandex.practicum.filmorate.db.dao.entity;

import lombok.*;
import ru.yandex.practicum.filmorate.db.enums.MotionPictureAssociationRate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MpaDto {

    private int id;
    private MotionPictureAssociationRate rate;
}
