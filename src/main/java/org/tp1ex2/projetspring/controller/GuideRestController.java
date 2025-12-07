package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.Guide;
import org.tp1ex2.projetspring.service.GuideService;

import java.util.List;

@RestController
@RequestMapping("/api/guide")
public class GuideRestController {

    @Autowired
    private GuideService guideService;

    @PostMapping("/save")
    public ResponseEntity<Guide> saveGuide(@RequestBody Guide guide) {
        try {
            Guide savedGuide = guideService.create(guide);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGuide);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Guide>> getAllGuides() {
        try {
            List<Guide> guides = guideService.getAll();
            return ResponseEntity.ok(guides);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getone/{id}")
    public ResponseEntity<Guide> getGuide(@PathVariable("id") Long id) {
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
    public ResponseEntity<List<Guide>> getGuidesByRating(@PathVariable("minRating") Double minRating) {
        try {
            List<Guide> guides = guideService.getAll();
            return ResponseEntity.ok(guides);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Guide> updateGuide(@RequestBody Guide guide, @PathVariable("id") Long id) {
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
    public ResponseEntity<Void> deleteGuide(@PathVariable("id") Long id) {
        try {
            guideService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
