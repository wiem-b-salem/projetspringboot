package org.tp1ex2.projetspring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tp1ex2.projetspring.model.Guide;
import org.tp1ex2.projetspring.model.User;
import org.tp1ex2.projetspring.security.JwtUtil;
import org.tp1ex2.projetspring.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Get user from database
            User user = userService.findByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail(), user.getType());

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "type", user.getType()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Authentication failed or other error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> registerRequest) {
        try {
            String email = registerRequest.get("email");
            String password = registerRequest.get("password");
            String firstName = registerRequest.get("firstName");
            String lastName = registerRequest.get("lastName");
            String phoneNumber = registerRequest.get("phoneNumber");
            String type = registerRequest.getOrDefault("type", "CLIENT");

            // Check if user already exists
            User existingUser = userService.findByEmail(email);
            if (existingUser != null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Email already registered");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Create new user
            User newUser;
            if ("GUIDE".equals(type)) {
                newUser = new Guide();
            } else {
                newUser = new User();
            }

            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setPhoneNumber(phoneNumber);
            newUser.setType(type);

            User createdUser = userService.create(newUser);

            // Generate JWT token
            String token = jwtUtil.generateToken(createdUser.getEmail(), createdUser.getType());

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("user", Map.of(
                    "id", createdUser.getId(),
                    "email", createdUser.getEmail(),
                    "firstName", createdUser.getFirstName(),
                    "lastName", createdUser.getLastName(),
                    "type", createdUser.getType()
            ));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String email = jwtUtil.extractUsername(token);
                
                if (jwtUtil.validateToken(token, email)) {
                    User user = userService.findByEmail(email);
                    if (user != null) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("valid", true);
                        response.put("user", Map.of(
                                "id", user.getId(),
                                "email", user.getEmail(),
                                "firstName", user.getFirstName(),
                                "lastName", user.getLastName(),
                                "type", user.getType()
                        ));
                        return ResponseEntity.ok(response);
                    }
                }
            }
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}

