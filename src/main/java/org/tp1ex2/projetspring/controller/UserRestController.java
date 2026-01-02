package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.User;
import org.tp1ex2.projetspring.dto.UserDTO;
import org.tp1ex2.projetspring.security.JwtUtil;
import org.tp1ex2.projetspring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private boolean isAdminAuthenticated(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        try {
            return jwtUtil.isAdminToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    private UserDTO convertToDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), 
                          user.getEmail(), user.getPhoneNumber(), user.getType());
    }

    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@RequestBody User user, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            logger.info("Saving user: {} {}", user.getFirstName(), user.getLastName());
            User savedUser = userService.create(user);
            logger.info("User saved successfully with ID: {}", savedUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedUser));
        } catch (Exception e) {
            logger.error("Error saving user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<User> users = userService.getAll();
            List<UserDTO> dtos = users.stream().map(this::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            logger.error("Error getting all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getone/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            User user = userService.getById(id);
            if (user != null) {
                return ResponseEntity.ok(convertToDTO(user));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable("email") String email, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            User user = userService.findByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(convertToDTO(user));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting user by email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user, @PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            User existingUser = userService.getById(id);
            if (existingUser != null) {
                user.setId(id);
                User updatedUser = userService.update(user);
                return ResponseEntity.ok(convertToDTO(updatedUser));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error updating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
