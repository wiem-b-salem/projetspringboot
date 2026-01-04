package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.User;
import org.tp1ex2.projetspring.model.Review;
import org.tp1ex2.projetspring.model.Place;
import org.tp1ex2.projetspring.service.ReviewService;
import org.tp1ex2.projetspring.service.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/review")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PlaceService placeService;

    /**
     * Add a review for a place
     */
    @PostMapping("/add-place/{placeId}")
    @ResponseBody
    public ResponseEntity<?> addPlaceReview(
            @PathVariable Long placeId,
            @RequestParam Integer stars,
            @RequestParam(required = false) String comment,
            HttpSession session) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        // Check if user is logged in
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "Please login first"));
        }

        try {
            // Validate stars
            if (stars < 1 || stars > 5) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Rating must be between 1 and 5"));
            }

            Place place = placeService.getPlaceById(placeId);
            if (place == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Place not found"));
            }

            // Create new review
            Review review = new Review();
            review.setUser(loggedInUser);
            review.setPlace(place);
            review.setStars(stars);
            review.setDescription(comment != null ? comment : "");
            
            reviewService.createReview(review);

            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "Review added successfully",
                "averageRating", reviewService.getAverageRatingForPlace(placeId)
            ));
        } catch (Exception e) {
            logger.error("Error adding review for place", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error adding review"));
        }
    }

    /**
     * Get all reviews for a place
     */
    @GetMapping("/place/{placeId}")
    @ResponseBody
    public ResponseEntity<?> getPlaceReviews(@PathVariable Long placeId) {
        try {
            Place place = placeService.getPlaceById(placeId);
            if (place == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Place not found"));
            }

            List<Review> reviews = reviewService.getReviewsForPlace(placeId);
            Double averageRating = reviewService.getAverageRatingForPlace(placeId);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "reviews", reviews,
                "averageRating", averageRating != null ? averageRating : 0.0,
                "totalReviews", reviews.size()
            ));
        } catch (Exception e) {
            logger.error("Error getting reviews for place", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error fetching reviews"));
        }
    }

    /**
     * Get average rating for a place
     */
    @GetMapping("/average/{placeId}")
    @ResponseBody
    public ResponseEntity<?> getAverageRating(@PathVariable Long placeId) {
        try {
            Place place = placeService.getPlaceById(placeId);
            if (place == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Place not found"));
            }

            Double averageRating = reviewService.getAverageRatingForPlace(placeId);
            List<Review> reviews = reviewService.getReviewsForPlace(placeId);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "averageRating", averageRating != null ? averageRating : 0.0,
                "totalReviews", reviews.size()
            ));
        } catch (Exception e) {
            logger.error("Error getting average rating", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error fetching rating"));
        }
    }

    /**
     * Delete a review
     */
    @PostMapping("/delete/{reviewId}")
    @ResponseBody
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "Please login first"));
        }

        try {
            Review review = reviewService.getReviewById(reviewId);
            if (review == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Review not found"));
            }

            // Check if user owns this review
            if (!review.getUser().getId().equals(loggedInUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "Cannot delete other users' reviews"));
            }

            reviewService.deleteReview(reviewId);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Review deleted successfully",
                "averageRating", reviewService.getAverageRatingForPlace(review.getPlace().getId())
            ));
        } catch (Exception e) {
            logger.error("Error deleting review", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error deleting review"));
        }
    }
}
