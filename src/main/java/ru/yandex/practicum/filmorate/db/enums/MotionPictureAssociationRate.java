package ru.yandex.practicum.filmorate.db.enums;

public enum MotionPictureAssociationRate {
    G("G - у фильма нет возрастных ограничений"),
    PG("PG - детям рекомендуется смотреть фильм с родителями"),
    PG13("PG13 - детям до 13 лет просмотр не желателен"),
    R("R - лицам до 17 лет просматривать фильм можно только в присутствии взрослого"),
    NC17("NC17 - лицам до 18 лет просмотр запрещён");

    private String description;

    MotionPictureAssociationRate(String description) {
        this.description = description;
    }
}
