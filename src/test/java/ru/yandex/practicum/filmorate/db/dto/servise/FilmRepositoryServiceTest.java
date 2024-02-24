package ru.yandex.practicum.filmorate.db.dto.servise;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.TestBasic;
import ru.yandex.practicum.filmorate.db.dto.entity.FilmDto;
import ru.yandex.practicum.filmorate.db.dto.entity.GenreDto;
import ru.yandex.practicum.filmorate.db.dto.entity.UserDto;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FilmRepositoryServiceTest extends TestBasic {

    private List<String> filmGenres = List.of("Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик");

    private List<String> mpaRating = List.of("G", "PG", "PG-13", "R", "NC-17");

    @Test
    public void createAndGetFilmListTest() {
        FilmDto filmDto = getRandomFilmDto();
        filmService.addFilm(filmDto);

        var oneFilmList = filmService.getFilmList();

        assertAll(
                () -> assertNotNull(oneFilmList),
                () -> assertEquals(1, oneFilmList.size()),
                () -> assertEquals(filmDto.getName(), oneFilmList.get(0).getName()),
                () -> assertEquals(filmDto.getDuration(), oneFilmList.get(0).getDuration()),
                () -> assertEquals(filmDto.getMpa().getId(), oneFilmList.get(0).getMpa().getId()),
                () -> assertEquals(filmDto.getDescription(), oneFilmList.get(0).getDescription())
        );
    }

    @Test
    public void getFilmByIdTest() {
        List<FilmDto> filmDtoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            filmDtoList.add(filmService.addFilm(getRandomFilmDto()));
        }

        var filmDBList = filmService.getFilmList();
        int randomId = random.nextInt(4) + 1;
        var filmById = filmService.getFilmById(randomId);

        Throwable fakeId1 = assertThrows(RuntimeException.class, () -> filmService.getFilmById(-1));
        Throwable fakeId2 = assertThrows(RuntimeException.class, () -> filmService.getFilmById(9999999));

        assertAll(
                () -> assertNotNull(filmDBList),
                () -> assertEquals(5, filmDBList.size()),
                () -> assertEquals(filmDtoList.get(0).getName(), filmDBList.get(0).getName()),
                () -> assertEquals(filmDtoList.get(1).getName(), filmDBList.get(1).getName()),
                () -> assertEquals(filmDtoList.get(2).getName(), filmDBList.get(2).getName()),
                () -> assertEquals(filmDtoList.get(3).getName(), filmDBList.get(3).getName()),
                () -> assertEquals(filmDtoList.get(4).getName(), filmDBList.get(4).getName()),
                () -> assertEquals("Фильм с id -1 не найден", fakeId1.getMessage()),
                () -> assertEquals("Фильм с id 9999999 не найден", fakeId2.getMessage()),
                () -> assertEquals(filmById, filmDBList.get(randomId - 1))
        );
    }

    @Test
    public void updateFilmTest() {
        FilmDto filmWithId = filmService.addFilm(getRandomFilmDto());

        var randomGenresWithCopies = getGenres(10);
        Set<GenreDto> genresWithoutCopies = new HashSet<>(randomGenresWithCopies);

        filmWithId.setReleaseDate(filmWithId.getReleaseDate().plusDays(100));
        filmWithId.setMpa(getMpaDto(2));
        filmWithId.setName(filmWithId.getName() + " updated");
        filmWithId.setGenres(randomGenresWithCopies);
        filmService.updateFilm(filmWithId);

        var updatedFilm = filmService.getFilmById(filmWithId.getId());

        FilmDto noIdFilm = getRandomFilmDto();
        FilmDto notExistedIdFilm = getRandomFilmDto();
        notExistedIdFilm.setId(9999999);
        Throwable nonExistentId1 = assertThrows(RuntimeException.class, () -> filmService.updateFilm(noIdFilm));
        Throwable nonExistentId2 = assertThrows(RuntimeException.class, () -> filmService.updateFilm(notExistedIdFilm));

        assertAll(
                () -> assertNotNull(updatedFilm),
                () -> assertEquals(filmWithId, updatedFilm),
                () -> assertEquals(new ArrayList<>(genresWithoutCopies), updatedFilm.getGenres()),
                () -> assertEquals("Внутренняя ошибка: Ошибка обновления данных. Фильма с id 0 не существует.", nonExistentId1.getMessage()),
                () -> assertEquals("Внутренняя ошибка: Ошибка обновления данных. Фильма с id 9999999 не существует.", nonExistentId2.getMessage())
        );
    }

    @Test
    public void deleteFilmTest() {
        FilmDto filmWithId = filmService.addFilm(getRandomFilmDto());

        var filmFromBd = filmService.getFilmById(filmWithId.getId());
        var filmListBeforeDeleteFilm = filmService.getFilmList();

        filmService.deleteFilm(filmFromBd.getId());
        var filmListAfterDeleteFilm = filmService.getFilmList();

        Throwable fakeId1 = assertThrows(RuntimeException.class, () -> filmService.deleteFilm(-1));
        Throwable fakeid2 = assertThrows(RuntimeException.class, () -> filmService.deleteFilm(99999));

        assertAll(
                () -> assertNotNull(filmFromBd),
                () -> assertNotNull(filmListAfterDeleteFilm),
                () -> assertEquals(filmWithId, filmFromBd),
                () -> assertTrue(filmListBeforeDeleteFilm.contains(filmFromBd)),
                () -> assertFalse(filmListAfterDeleteFilm.contains(filmFromBd)),
                () -> assertEquals("Ошибка удаления фильма: фильм -1 не найден", fakeId1.getMessage()),
                () -> assertEquals("Ошибка удаления фильма: фильм 99999 не найден", fakeid2.getMessage())
        );
    }

    @Test
    public void addAndDeleteFilmUserLikesTest() {
        List<UserDto> someRandomUsers = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            someRandomUsers.add(userService.addUser(getRandomUserDto()));
        }

        long filmId = filmService.addFilm(getRandomFilmDto()).getId();

        for (UserDto someRandomUser : someRandomUsers) {
            filmService.addUserLike(filmId, someRandomUser.getId());
        }

        Throwable addDoubleLikeFromUser = assertThrows(RuntimeException.class, () -> filmService.addUserLike(filmId, someRandomUsers.get(1).getId()));

        var userLikes = filmService.getFilmById(filmId).getUserLikes();

        filmService.deleteUserLike(filmId, someRandomUsers.get(0).getId());
        filmService.deleteUserLike(filmId, someRandomUsers.get(2).getId());
        filmService.deleteUserLike(filmId, someRandomUsers.get(4).getId());

        var userLikesAfterDeleteThreeOfIt = filmService.getFilmById(filmId).getUserLikes();

        filmService.deleteUserLike(filmId, someRandomUsers.get(1).getId());
        filmService.deleteUserLike(filmId, someRandomUsers.get(3).getId());
        filmService.deleteUserLike(filmId, someRandomUsers.get(5).getId());

        var emptyUserLikes = filmService.getFilmById(filmId).getUserLikes();

        Throwable addFakeUserLike1 = assertThrows(RuntimeException.class, () -> filmService.addUserLike(filmId, -1));
        Throwable addfakeuserlike2 = assertThrows(RuntimeException.class, () -> filmService.addUserLike(filmId, 99999));
        Throwable deletefakeuserlike1 = assertThrows(RuntimeException.class, () -> filmService.deleteUserLike(filmId, -1));
        Throwable deletefakeuserlike2 = assertThrows(RuntimeException.class, () -> filmService.deleteUserLike(filmId, 99999));

        Throwable addfakefilmlike1 = assertThrows(RuntimeException.class, () -> filmService.addUserLike(-1, someRandomUsers.get(1).getId()));
        Throwable addfakefilmlike2 = assertThrows(RuntimeException.class, () -> filmService.addUserLike(9999, someRandomUsers.get(2).getId()));
        Throwable deletefakefilmlike1 = assertThrows(RuntimeException.class, () -> filmService.deleteUserLike(-1, someRandomUsers.get(1).getId()));
        Throwable deletefakefilmlike2 = assertThrows(RuntimeException.class, () -> filmService.deleteUserLike(99999, someRandomUsers.get(2).getId()));

        assertAll(
                () -> assertNotNull(userLikes),
                () -> assertNotNull(userLikesAfterDeleteThreeOfIt),
                () -> assertNotNull(emptyUserLikes),

                () -> assertTrue(userLikes.contains(someRandomUsers.get(0))),
                () -> assertTrue(userLikes.contains(someRandomUsers.get(1))),
                () -> assertTrue(userLikes.contains(someRandomUsers.get(2))),
                () -> assertTrue(userLikes.contains(someRandomUsers.get(3))),
                () -> assertTrue(userLikes.contains(someRandomUsers.get(4))),
                () -> assertTrue(userLikes.contains(someRandomUsers.get(5))),

                () -> assertFalse(userLikesAfterDeleteThreeOfIt.contains(someRandomUsers.get(0))),
                () -> assertTrue(userLikesAfterDeleteThreeOfIt.contains(someRandomUsers.get(1))),
                () -> assertFalse(userLikesAfterDeleteThreeOfIt.contains(someRandomUsers.get(2))),
                () -> assertTrue(userLikesAfterDeleteThreeOfIt.contains(someRandomUsers.get(3))),
                () -> assertFalse(userLikesAfterDeleteThreeOfIt.contains(someRandomUsers.get(4))),
                () -> assertTrue(userLikesAfterDeleteThreeOfIt.contains(someRandomUsers.get(5))),

                () -> assertEquals(0, emptyUserLikes.size()),

                () -> assertEquals("Ошибка добавления лайка фильму: этот пользователь уже ставил лайк ", addDoubleLikeFromUser.getMessage()),

                () -> assertEquals("Пользователя с id -1 не существует", addFakeUserLike1.getMessage()),
                () -> assertEquals("Пользователя с id 99999 не существует", addfakeuserlike2.getMessage()),
                () -> assertEquals("Пользователя с id -1 не существует", deletefakeuserlike1.getMessage()),
                () -> assertEquals("Пользователя с id 99999 не существует", deletefakeuserlike2.getMessage()),

                () -> assertEquals("Ошибка добавления лайка фильму: фильм -1 не найден", addfakefilmlike1.getMessage()),
                () -> assertEquals("Ошибка добавления лайка фильму: фильм 9999 не найден", addfakefilmlike2.getMessage()),
                () -> assertEquals("Ошибка удаления лайка у фильма: фильм -1 не найден", deletefakefilmlike1.getMessage()),
                () -> assertEquals("Ошибка удаления лайка у фильма: фильм 99999 не найден", deletefakefilmlike2.getMessage())
        );
    }

    @Test
    public void getTopRateTest() {
        List<UserDto> userList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            userList.add(userService.addUser(getRandomUserDto()));
        }

        List<FilmDto> filmList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            filmList.add(filmService.addFilm(getRandomFilmDto()));
        }

        int randomId = random.nextInt(7);
        int randomId2 = random.nextInt(5) + 8;
        int randomId3 = random.nextInt(5) + 14;

        List<FilmDto> topBasic = new ArrayList<>(3);
        topBasic.add(filmList.get(randomId));
        topBasic.add(filmList.get(randomId2));
        topBasic.add(filmList.get(randomId3));

        addRandomUserLikes(topBasic.get(0), userList);
        addRandomUserLikes(topBasic.get(1), userList);
        addRandomUserLikes(topBasic.get(2), userList);
        topBasic.sort(Comparator.comparingInt(o -> o.getUserLikes().size()));
        Collections.reverse(topBasic);

        List<FilmDto> topRate = filmService.getTopRate(5);

        assertAll(
                () -> assertNotNull(topRate),
                () -> assertEquals(topBasic.get(0).getId(), topRate.get(0).getId()),
                () -> assertEquals(topBasic.get(1).getId(), topRate.get(1).getId()),
                () -> assertEquals(topBasic.get(2).getId(), topRate.get(2).getId())
        );
    }

    @Test
    public void getGenresListTest() {
        var genreDtoList = filmService.getGenresList();

        assertAll(
                () -> assertNotNull(genreDtoList),
                () -> assertEquals(6, genreDtoList.size()),
                () -> assertEquals(filmGenres.get(0), genreDtoList.get(0).getName()),
                () -> assertEquals(filmGenres.get(1), genreDtoList.get(1).getName()),
                () -> assertEquals(filmGenres.get(2), genreDtoList.get(2).getName()),
                () -> assertEquals(filmGenres.get(3), genreDtoList.get(3).getName()),
                () -> assertEquals(filmGenres.get(4), genreDtoList.get(4).getName()),
                () -> assertEquals(filmGenres.get(5), genreDtoList.get(5).getName())
        );
    }

    @Test
    public void getGenreByIdTest() {
        int genreId = random.nextInt(5);
        String genreName = filmGenres.get(genreId);

        Throwable fakeid1 = assertThrows(RuntimeException.class, () -> filmService.getGenreById(-1));
        Throwable fakeid2 = assertThrows(RuntimeException.class, () -> filmService.getGenreById(9999999));

        var mpaDto = filmService.getGenreById(genreId + 1);
        assertAll(
                () -> assertNotNull(mpaDto),
                () -> assertEquals(genreName, mpaDto.getName()),
                () -> assertEquals("Жанр не существует", fakeid1.getMessage()),
                () -> assertEquals("Жанр не существует", fakeid2.getMessage())
        );
    }

    @Test
    public void getMPAListTest() {
        var mpa = filmService.getMpaList();

        assertAll(
                () -> assertEquals(5, mpa.size()),
                () -> assertEquals(mpaRating.get(0), mpa.get(0).getName()),
                () -> assertEquals(mpaRating.get(1), mpa.get(1).getName()),
                () -> assertEquals(mpaRating.get(2), mpa.get(2).getName()),
                () -> assertEquals(mpaRating.get(3), mpa.get(3).getName()),
                () -> assertEquals(mpaRating.get(4), mpa.get(4).getName())
        );
    }

    @Test
    public void getMPAByIdTest() {
        int randMpa = random.nextInt(4);
        String mpaRank = mpaRating.get(randMpa);
        var mpaDto = filmService.getMpaById(randMpa + 1);

        Throwable fakeid1 = assertThrows(RuntimeException.class, () -> filmService.getMpaById(-1));
        Throwable fakeid2 = assertThrows(RuntimeException.class, () -> filmService.getMpaById(9999999));

        assertAll(
                () -> assertNotNull(mpaDto),
                () -> assertEquals(mpaRank, mpaDto.getName()),
                () -> assertEquals("Рейтинг не найден", fakeid1.getMessage()),
                () -> assertEquals("Рейтинг не найден", fakeid2.getMessage())
        );
    }

    private void addRandomUserLikes(FilmDto filmDto, List<UserDto> userList) {
        for (UserDto user : userList) {
            int yesOrNot = random.nextInt(100);
            if (yesOrNot <= 50) {
                filmService.addUserLike(filmDto.getId(), user.getId());
                filmDto.getUserLikes().add(user);
            }
        }
    }
}