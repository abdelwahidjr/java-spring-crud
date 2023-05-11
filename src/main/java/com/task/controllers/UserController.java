package com.task.controllers;

import com.task.apierror.EntityNotFoundException;
import com.task.models.User;
import com.task.payload.request.UpdateUserRequest;
import com.task.repository.UserRepository;
import com.task.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Properties;


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

    @GetMapping("/sendmail")
    public String welcomeEmail() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("sandbox.smtp.mailtrap.io");
        mailSender.setPort(2525);
        mailSender.setUsername("2fb7bc5a660bfa");
        mailSender.setPassword("3e438a14bd634a");

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        mailSender.setJavaMailProperties(properties);

        String from = "abdelwahidjr@gmail.com";
        String to = "abdelwahidjr@gmail.com";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Welcome");
        message.setText("Welcome From App");
        mailSender.send(message);
        return "Email sent successfully";
    }
}

