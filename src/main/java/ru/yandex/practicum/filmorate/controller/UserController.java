package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servise.UserStorageService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorageService userStorageService;

    @Autowired
    public UserController(UserStorageService userStorageService) {
        this.userStorageService = userStorageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userStorageService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok(userStorageService.getUserList());
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        var addedUser = userStorageService.addUser(user);
        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userStorageService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long userId) {
        return ResponseEntity.ok("Пользователь удален");
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(
            @PathVariable("id") long userId,
            @PathVariable long friendId) {

        return ResponseEntity.ok(userStorageService.addFriend(userId, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFriend(
            @PathVariable("id") long userId,
            @PathVariable long friendId) {

        return ResponseEntity.ok(userStorageService.deleteFriend(userId, friendId));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriendList(@PathVariable("id") long userId) {
        return ResponseEntity.ok(userStorageService.getFriendList(userId));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriendList(
            @PathVariable("id") long userId,
            @PathVariable long otherId) {

        return ResponseEntity.ok(userStorageService.getCommonFriendList(userId, otherId));
    }


}
