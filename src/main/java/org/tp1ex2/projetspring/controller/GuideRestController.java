package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.Guide;
import org.tp1ex2.projetspring.security.JwtUtil;
import org.tp1ex2.projetspring.service.GuideService;

import java.util.List;

@RestController
@RequestMapping("/api/guide")
public class GuideRestController {

    @Autowired
    private GuideService guideService;

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
    public ResponseEntity<Guide> saveGuide(@RequestBody Guide guide, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Guide savedGuide = guideService.create(guide);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGuide);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Guide>> getAllGuides(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<Guide> guides = guideService.getAll();
            return ResponseEntity.ok(guides);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getone/{id}")
    public ResponseEntity<Guide> getGuide(@PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Guide guide = guideService.getById(id);
            if (guide != null) {
                return ResponseEntity.ok(guide);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/rating/{minRating}")
    public ResponseEntity<List<Guide>> getGuidesByRating(@PathVariable("minRating") Double minRating, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<Guide> guides = guideService.getAll();
            return ResponseEntity.ok(guides);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Guide> updateGuide(@RequestBody Guide guide, @PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Guide existingGuide = guideService.getById(id);
            if (existingGuide != null) {
                guide.setId(id);
                Guide updatedGuide = guideService.update(guide);
                return ResponseEntity.ok(updatedGuide);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuide(@PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            guideService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
