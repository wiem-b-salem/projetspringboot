package org.tp1ex2.projetspring.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tp1ex2.projetspring.model.Tour;
import org.tp1ex2.projetspring.repository.TourRepository;
import org.tp1ex2.projetspring.service.TourService;

import java.time.LocalDate;
import java.util.List;

@Service
public class TourServiceImpl implements TourService {

    @Autowired
    private TourRepository tourRepository;

    @Override
    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    @Override
    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    @Override
    public List<Tour> getAll() {
        return getAllTours();
    }

    @Override
    public Tour getTourById(Long id) {
        return tourRepository.findById(id).orElse(null);
    }

    @Override
    public Tour updateTour(Tour tour) {
        return tourRepository.saveAndFlush(tour);
    }

    @Override
    public void deleteTour(Long id) {
        tourRepository.deleteById(id);
    }

    @Override
    public List<Tour> filterByMaxPrice(Double maxPrice) {
        return tourRepository.findByMaxPrice(maxPrice);
    }

    @Override
    public List<Tour> filterByPriceRange(Double minPrice, Double maxPrice) {
        return tourRepository.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Tour> getUpcomingTours(LocalDate startDate) {
        return tourRepository.findUpcomingTours(startDate);
    }

    @Override
    public List<Tour> searchTours(String keyword) {
        return tourRepository.searchByKeyword(keyword);
    }

    @Override
    public List<Tour> filterTours(String keyword, Double minPrice, Double maxPrice, Boolean upcoming, LocalDate startDate) {
        return tourRepository.filterTours(keyword, minPrice, maxPrice, upcoming, startDate);
    }
}
