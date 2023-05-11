package com.task.services;

import com.task.apierror.EntityNotFoundException;
import com.task.models.User;
import com.task.payload.request.UpdateUserRequest;
import com.task.repository.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // testing purpose
    public Optional<User> getUserNoException(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers(@RequestParam(required = false) String username) {
        List<User> users = new ArrayList<User>();

        if (username == null) userRepository.findAll().forEach(users::add);
        else userRepository.findByUsernameContaining(username).forEach(users::add);
        if (users.isEmpty()) {
            throw new EntityNotFoundException(User.class);
        }
        return users;
    }

    public Optional<User> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user;
        } else {
            throw new EntityNotFoundException(User.class, "id", id.toString());
        }
    }

    public ResponseEntity updateUser(@PathVariable("id") long id, @RequestBody UpdateUserRequest updateUserRequest) {
        JSONObject response = new JSONObject();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User _user = user.get();
            _user.setUsername(updateUserRequest.getUsername());
            _user.setEmail(updateUserRequest.getEmail());
            userRepository.save(_user);
            response.put("message", "user has been updated");
            response.put("data", _user);
            return new ResponseEntity(response, HttpStatus.OK);
        } else {
            response.put("message", "not acceptable user not found");
            return new ResponseEntity(response, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity deleteUser(@RequestParam Long[] ids) {
        JSONObject response = new JSONObject();
        try {
            for (Long id : ids) {
                Optional<User> user = userRepository.findById(id);
                if (user.isPresent()) userRepository.deleteById(id);
            }
            response.put("message", "All users have been deleted");
            return new ResponseEntity(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            response.put("message", "error while deleting some users");
            return new ResponseEntity(response, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    public void welcomeEmail() throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mailosaur.net");
        props.put("mail.smtp.port", "2525");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.mailosaur.net");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gbalvr1h@mailosaur.net", "k4dywOgVM4c7IAiCEgz0I0NiBr3ojBwq");
            }
        });

        MimeMessage message = new MimeMessage(session);

        message.setSubject("A test email");
        message.setFrom(new InternetAddress("MOHAMED ABDELWAHID <abdelwahidjr@gmail.com>"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("MOHAMED ABDELWAHID <abdelwahidjr@gmail.com>"));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent("<p>Hello world.</p>", "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        session.getTransport("smtp").send(message);

    }
}
