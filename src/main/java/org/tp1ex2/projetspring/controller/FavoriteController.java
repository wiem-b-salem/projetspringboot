package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.User;
import org.tp1ex2.projetspring.model.Favorite;
import org.tp1ex2.projetspring.model.Place;
import org.tp1ex2.projetspring.model.Tour;
import org.tp1ex2.projetspring.service.FavoriteService;
import org.tp1ex2.projetspring.service.PlaceService;
import org.tp1ex2.projetspring.service.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/favorite")
public class FavoriteController {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private TourService tourService;

    /**
     * Add a place to favorites
     */
    @PostMapping("/add-place/{placeId}")
    @ResponseBody
    public ResponseEntity<?> addPlaceToFavorite(@PathVariable Long placeId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        // Check if user is logged in
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "Please login first"));
        }

        try {
            Place place = placeService.getPlaceById(placeId);
            if (place == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Place not found"));
            }

            // Check if already favorited
            List<Favorite> existing = favoriteService.getUserFavorites(loggedInUser.getId());
            boolean alreadyFavorited = existing.stream()
                .anyMatch(f -> f.getPlace() != null && f.getPlace().getId().equals(placeId));

            if (alreadyFavorited) {
                return ResponseEntity.ok(Map.of("success", false, "message", "Already in favorites", "favorited", true));
            }

            // Create new favorite
            Favorite favorite = new Favorite();
            favorite.setUser(loggedInUser);
            favorite.setPlace(place);
            favoriteService.createFavorite(favorite);

            return ResponseEntity.ok(Map.of("success", true, "message", "Added to favorites", "favorited", true));
        } catch (Exception e) {
            logger.error("Error adding place to favorites", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error adding to favorites"));
        }
    }

    /**
     * Add a tour to favorites
     */
    @PostMapping("/add-tour/{tourId}")
    @ResponseBody
    public ResponseEntity<?> addTourToFavorite(@PathVariable Long tourId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        // Check if user is logged in
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "Please login first"));
        }

        try {
            Tour tour = tourService.getTourById(tourId);
            if (tour == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Tour not found"));
            }

            // Check if already favorited
            List<Favorite> existing = favoriteService.getUserFavorites(loggedInUser.getId());
            boolean alreadyFavorited = existing.stream()
                .anyMatch(f -> f.getTour() != null && f.getTour().getId().equals(tourId));

            if (alreadyFavorited) {
                return ResponseEntity.ok(Map.of("success", false, "message", "Already in favorites", "favorited", true));
            }

            // Create new favorite
            Favorite favorite = new Favorite();
            favorite.setUser(loggedInUser);
            favorite.setTour(tour);
            favoriteService.createFavorite(favorite);

            return ResponseEntity.ok(Map.of("success", true, "message", "Added to favorites", "favorited", true));
        } catch (Exception e) {
            logger.error("Error adding tour to favorites", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error adding to favorites"));
        }
    }

    /**
     * Remove from favorites
     */
    @PostMapping("/remove/{favoriteId}")
    @ResponseBody
    public ResponseEntity<?> removeFromFavorite(@PathVariable Long favoriteId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "Please login first"));
        }

        try {
            Favorite favorite = favoriteService.getFavoriteById(favoriteId);
            if (favorite == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Favorite not found"));
            }

            // Check ownership
            if (!favorite.getUser().getId().equals(loggedInUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "Unauthorized"));
            }

            favoriteService.deleteFavorite(favoriteId);

            return ResponseEntity.ok(Map.of("success", true, "message", "Removed from favorites", "favorited", false));
        } catch (Exception e) {
            logger.error("Error removing from favorites", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error removing from favorites"));
        }
    }

    /**
     * Check if a place is favorited
     */
    @GetMapping("/check-place/{placeId}")
    @ResponseBody
    public ResponseEntity<?> checkPlaceFavorited(@PathVariable Long placeId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return ResponseEntity.ok(Map.of("favorited", false));
        }

        try {
            List<Favorite> favorites = favoriteService.getUserFavorites(loggedInUser.getId());
            boolean isFavorited = favorites.stream()
                .anyMatch(f -> f.getPlace() != null && f.getPlace().getId().equals(placeId));

            return ResponseEntity.ok(Map.of("favorited", isFavorited));
        } catch (Exception e) {
            logger.error("Error checking favorite status", e);
            return ResponseEntity.ok(Map.of("favorited", false));
        }
    }

    /**
     * Check if a tour is favorited
     */
    @GetMapping("/check-tour/{tourId}")
    @ResponseBody
    public ResponseEntity<?> checkTourFavorited(@PathVariable Long tourId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return ResponseEntity.ok(Map.of("favorited", false));
        }

        try {
            List<Favorite> favorites = favoriteService.getUserFavorites(loggedInUser.getId());
            boolean isFavorited = favorites.stream()
                .anyMatch(f -> f.getTour() != null && f.getTour().getId().equals(tourId));

            return ResponseEntity.ok(Map.of("favorited", isFavorited));
        } catch (Exception e) {
            logger.error("Error checking favorite status", e);
            return ResponseEntity.ok(Map.of("favorited", false));
        }
    }
}
