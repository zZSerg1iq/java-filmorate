package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.servise.UserDataStorageService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserDataStorageService userStorageService;

    @Autowired
    public UserController(UserDataStorageService userStorageService) {
        this.userStorageService = userStorageService;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        var user = userStorageService.getUser(id);
        if (user.getId() != -1) {
            return ResponseEntity.ok(user);
        }
        return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUserList() {
        var userList = userStorageService.getUserList();
        return ResponseEntity.ok(userList);
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        var addedUser = userStorageService.addUser(user);
        log.info("User added: " + addedUser);
        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        var updateResult = userStorageService.updateUser(user);
        if (updateResult.getId() != -1) {
            log.info("User updated: " + updateResult);
            return ResponseEntity.ok(updateResult);
        }
        log.info("error update user: " + user);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userStorageService.deleteUser(id);
    }


}
