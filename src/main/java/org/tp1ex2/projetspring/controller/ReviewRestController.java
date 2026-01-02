package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.Review;
import org.tp1ex2.projetspring.security.JwtUtil;
import org.tp1ex2.projetspring.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;

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
    public ResponseEntity<Review> saveReview(@RequestBody Review review, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Review savedReview = reviewService.create(review);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<Review> reviews = reviewService.getAll();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getone/{id}")
    public ResponseEntity<Review> getReview(@PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Review review = reviewService.getById(id);
            if (review != null) {
                return ResponseEntity.ok(review);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable("userId") Long userId, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<Review> reviews = reviewService.getAll();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/tour/{tourId}")
    public ResponseEntity<List<Review>> getReviewsByTour(@PathVariable("tourId") Long tourId, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<Review> reviews = reviewService.getAll();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Review> updateReview(@RequestBody Review review, @PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Review existingReview = reviewService.getById(id);
            if (existingReview != null) {
                review.setId(id);
                Review updatedReview = reviewService.update(review);
                return ResponseEntity.ok(updatedReview);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdminAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            reviewService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
