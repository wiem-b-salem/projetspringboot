package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.Admin;
import org.tp1ex2.projetspring.security.JwtUtil;
import org.tp1ex2.projetspring.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private AdminService adminService;

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

    @PostMapping("/save")
    public ResponseEntity<Admin> saveAdmin(@RequestBody Admin admin, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Admin savedAdmin = adminService.create(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<Admin> admins = adminService.getAll();
            return ResponseEntity.ok(admins);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getone/{id}")
    public ResponseEntity<Admin> getAdmin(@PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Admin admin = adminService.getById(id);
            if (admin != null) {
                return ResponseEntity.ok(admin);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<Admin> getAdminByLogin(@PathVariable("login") String login, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Admin admin = adminService.findByLogin(login);
            if (admin != null) {
                return ResponseEntity.ok(admin);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Admin> updateAdmin(@RequestBody Admin admin, @PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Admin existingAdmin = adminService.getById(id);
            if (existingAdmin != null) {
                admin.setId(id);
                Admin updatedAdmin = adminService.update(admin);
                return ResponseEntity.ok(updatedAdmin);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            adminService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
