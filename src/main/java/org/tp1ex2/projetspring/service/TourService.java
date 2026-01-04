package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Tour;
import java.time.LocalDate;
import java.util.List;

public interface TourService {
    Tour createTour(Tour tour);
    List<Tour> getAllTours();
    List<Tour> getAll();
    Tour getTourById(Long id);
    Tour updateTour(Tour tour);
    void deleteTour(Long id);
    
    // Filter methods
    List<Tour> filterByMaxPrice(Double maxPrice);
    List<Tour> filterByPriceRange(Double minPrice, Double maxPrice);
    List<Tour> getUpcomingTours(LocalDate startDate);
    List<Tour> searchTours(String keyword);
    List<Tour> filterTours(String keyword, Double minPrice, Double maxPrice, Boolean upcoming, LocalDate startDate);
}
