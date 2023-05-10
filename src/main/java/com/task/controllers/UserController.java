package com.task.controllers;

import com.task.models.User;
import com.task.payload.request.UpdateUserRequest;
import com.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private JavaMailSender mailSender;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // get all
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String username) {
        try {
            List<User> users = new ArrayList<User>();

            if (username == null) userRepository.findAll().forEach(users::add);
            else userRepository.findByUsernameContaining(username).forEach(users::add);

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get by id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // update user
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody UpdateUserRequest updateUserRequest) {

        if (userRepository.existsByUsername(updateUserRequest.getUsername()) || userRepository.existsByEmail(updateUserRequest.getEmail())) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setUsername(updateUserRequest.getUsername());
            _user.setEmail(updateUserRequest.getEmail());
            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // delete users
    @DeleteMapping("/users/delete")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam Long[] ids) {

        try {
            for (Long id : ids) {
                Optional<User> userData = userRepository.findById(id);
                if (userData.isPresent()) userRepository.deleteById(id);
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

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

