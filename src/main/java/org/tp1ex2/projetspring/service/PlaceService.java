package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Place;
import java.util.List;

public interface PlaceService {
    Place createPlace(Place place);
    List<Place> getAllPlaces();
    List<Place> getAll();
    Place getPlaceById(Long id);
    Place updatePlace(Place place);
    void deletePlace(Long id);
    
    // Filter methods
    List<Place> filterByCategory(String category);
    List<Place> filterByCity(String city);
    List<Place> filterByCategoryAndCity(String category, String city);
    List<Place> searchPlaces(String keyword);
    List<String> getAllCities();
    List<String> getAllCategories();
}
