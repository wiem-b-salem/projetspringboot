package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.Favorite;
import org.tp1ex2.projetspring.service.FavoriteService;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteRestController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/save")
    public ResponseEntity<Favorite> saveFavorite(@RequestBody Favorite favorite) {
        try {
            Favorite savedFavorite = favoriteService.create(favorite);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFavorite);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Favorite>> getAllFavorites() {
        try {
            List<Favorite> favorites = favoriteService.getAll();
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getone/{id}")
    public ResponseEntity<Favorite> getFavorite(@PathVariable("id") Long id) {
        try {
            Favorite favorite = favoriteService.getById(id);
            if (favorite != null) {
                return ResponseEntity.ok(favorite);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorite>> getFavoritesByUser(@PathVariable("userId") Long userId) {
        try {
            List<Favorite> favorites = favoriteService.getAll();
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Favorite> updateFavorite(@RequestBody Favorite favorite, @PathVariable("id") Long id) {
        try {
            Favorite existingFavorite = favoriteService.getById(id);
            if (existingFavorite != null) {
                favorite.setId(id);
                Favorite updatedFavorite = favoriteService.update(favorite);
                return ResponseEntity.ok(updatedFavorite);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable("id") Long id) {
        try {
            favoriteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
