package com.example.expensesmanager.controller;

import com.example.expensesmanager.model.User;
import com.example.expensesmanager.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = new ArrayList<>();
            userRepo.findAll().forEach(users::add);

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userData = userRepo.findById(id);

        if(userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Long id) {
        userRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        if (userRepo.existsByName(user.getName()) ||
                userRepo.existsByEmail(user.getEmail()) ||
                userRepo.existsByPassword(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        User userObj = userRepo.save(user);
        return new ResponseEntity<>(userObj, HttpStatus.OK);
    }

    @PostMapping("/updateUserById/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> oldUserData = userRepo.findById(id);

        if(oldUserData.isPresent()) {
            User userObj = oldUserData.get();
            userObj.setName(user.getName());
            userObj.setEmail(user.getEmail());
            userObj.setPassword(user.getPassword());
            userObj.setExpenses(user.getExpenses());
            User userData = userRepo.save(userObj);
            return new ResponseEntity<>(userData, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/verifyUser")
    public ResponseEntity<?> verifyUser(@RequestBody User user) {
        User existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        } else {
            if (!existingUser.getPassword().equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
