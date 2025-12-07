package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Tour;
import java.util.List;

public interface TourService {
    Tour createTour(Tour tour);
    List<Tour> getAllTours();
    Tour getTourById(Long id);
    Tour updateTour(Tour tour);
    void deleteTour(Long id);
}
