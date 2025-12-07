package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.Tour;
import org.tp1ex2.projetspring.service.TourService;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
public class TourRestController {

    @Autowired
    private TourService tourService;

    @PostMapping("/save")
    public ResponseEntity<Tour> saveTour(@RequestBody Tour tour) {
        try {
            Tour savedTour = tourService.createTour(tour);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTour);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tour>> getAllTours() {
        try {
            List<Tour> tours = tourService.getAll();
            return ResponseEntity.ok(tours);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getone/{id}")
    public ResponseEntity<Tour> getTour(@PathVariable("id") Long id) {
        try {
            Tour tour = tourService.getTourById(id);
            if (tour != null) {
                return ResponseEntity.ok(tour);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/guide/{guideId}")
    public ResponseEntity<List<Tour>> getToursByGuide(@PathVariable("guideId") Long guideId) {
        try {
            List<Tour> tours = tourService.getAllTours();
            return ResponseEntity.ok(tours);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Tour> updateTour(@RequestBody Tour tour, @PathVariable("id") Long id) {
        try {
            Tour existingTour = tourService.getTourById(id);
            if (existingTour != null) {
                tour.setId(id);
                Tour updatedTour = tourService.updateTour(tour);
                return ResponseEntity.ok(updatedTour);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable("id") Long id) {
        try {
            tourService.deleteTour(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
