package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userService.getAllEntries());
    }

    @GetMapping("id/{id}")
    public User getUserbyId(@PathVariable ObjectId id) {
        return userService.findById(id).orElse(null);
    }

    @PostMapping
    public boolean createUser(@RequestBody User newUser) {
        userService.saveEntry(newUser);
        return true;
    }

    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser, @PathVariable String userName) {
        User userInDb = userService.findByUserName(userName);
        if(userInDb != null) {
            userInDb.setUserName(updatedUser.getUserName());
            userInDb.setPassword(updatedUser.getPassword());
            userService.saveEntry(userInDb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
