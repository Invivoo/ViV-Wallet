package com.invivoo.vivwallet.api.interfaces.users;

import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(UsersController.API_V_1_USERS)
public class UsersController {

    static final String API_V_1_USERS = "/api/v1/users";
    private final UserRepository userRepository;

    @Autowired
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.created(getLocation(savedUser))
                             .body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long userId) {
        return userRepository.findById(userId)
                             .map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound()
                                                   .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> putUser(@PathVariable("id") Long userId, @RequestBody UserUpdateDto userUpdate) {
        return userRepository.findById(userId)
                             .map(user -> updateUser(user, userUpdate))
                             .map(userRepository::save)
                             .map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound()
                                                   .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long userId) {
        return userRepository.findById(userId)
                             .map(this::deleteUser)
                             .map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound()
                                                   .build());
    }

    private User deleteUser(User user) {
        userRepository.delete(user);
        return user;
    }

    private URI getLocation(User savedUser) {
        return URI.create(String.format("%s/%s", API_V_1_USERS, savedUser.getId()));
    }

    private User updateUser(User user, UserUpdateDto userUpdate) {
        user.setFullName(userUpdate.getFullName());
        return user;
    }
}
