package com.task.controllers;

import com.task.apierror.EntityNotFoundException;
import com.task.models.User;
import com.task.payload.request.UpdateUserRequest;
import com.task.repository.UserRepository;
import com.task.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserService userService;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // get all
    @GetMapping("/users")
    public List<User> getAllUsers(@RequestParam(required = false) String username) {
        return userService.getAllUsers(username);
    }

    // get by id
    @GetMapping("/users/{id}")
    public Optional<User> getuser(@PathVariable("id") Long id) throws EntityNotFoundException {
        return userService.getUser(id);
    }

    // update user
    @PutMapping("/users/{id}")
    public ResponseEntity updateUser(@PathVariable("id") long id, @RequestBody UpdateUserRequest updateUserRequest) {
        return userService.updateUser(id, updateUserRequest);
    }

    // delete users
    @DeleteMapping("/users/delete")
    public ResponseEntity deleteUser(@RequestParam Long[] ids) {
        return userService.deleteUser(ids);
    }

}

