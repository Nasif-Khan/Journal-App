package com.nasif.jounalApp.controller;

import com.nasif.jounalApp.cache.AppCache;
import com.nasif.jounalApp.entity.JournalEntry;
import com.nasif.jounalApp.entity.User;
import com.nasif.jounalApp.repository.UserRepositoryImpl;
import com.nasif.jounalApp.service.JournalEntryService;
import com.nasif.jounalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name="4. Admin APIs", description = "Cannot be accessed by you, only admin can access. \nGet all registered users and email of users who opted in for mail. Create a new admin user and refreshes the cache. ")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/all-users")
    @Operation(summary = "Retrieves all the registered users in the journal application ")
    public ResponseEntity<?> getAllUsers() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    @Operation(summary = "Gives the user the ADMIN role")
    public void createUser(@RequestBody User user) {
        userService.saveAdmin(user);
    }

    @PutMapping("/update-user-to-admin/{username}")
    public ResponseEntity<User> updateUser(@PathVariable("username") String username) {
        User user = userService.findByUserName(username);
        if (user != null) {
            List<String> roles = user.getRoles();
            if (!roles.contains("ADMIN")) {
                userService.addAdmin(user);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/refresh-app-cache")
    @Operation(summary = "Cleans the cache memory and updates with fresh data")
    public void refreshAppCache() {
        appCache.init();
    }

    @GetMapping("/check-email")
    @Operation(summary = "Gets all the users who opted in for mail subscription")
    public ResponseEntity<List<User>> checkEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<User> usersForSA = userRepositoryImpl.getUsersForSA();
        return new ResponseEntity<>(usersForSA, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete-user/id/{id}")
    @Operation(summary = "Deletes users by user id")
    public ResponseEntity<Object> deleteUserById(@PathVariable ObjectId id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserById(id);
            if(user != null && !userRepositoryImpl.isAdmin(user)) {
                user.getJournalEntries().forEach(journalEntry -> {
                    String journalId = journalEntry.getId();
                    ObjectId jId = new ObjectId(journalId);
                    journalEntryService.deleteJournalEntryById(jId, user.getUserName());
                });
                userService.deleteUserById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>("Cannot delete an ADMIN",HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error in deleting", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/user/id/{id}")
    @Operation(summary = "Gets the user by the user id")
    public ResponseEntity<User> getUserById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserById(id);
        if(user != null ) {
            System.out.println(user.getUserName() + "is not ADMIN and can be deleted");
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/check-admin/{username}")
    public ResponseEntity<String> isAdmin(@PathVariable String username) {
        User user = userService.findByUserName(username);
        boolean admin = userRepositoryImpl.isAdmin(user);
        if(admin){
            return new ResponseEntity<>("IS ADMIN", HttpStatus.OK);
        }
        return new ResponseEntity<>("IS NOT ADMIN", HttpStatus.UNAUTHORIZED);
    }
}
