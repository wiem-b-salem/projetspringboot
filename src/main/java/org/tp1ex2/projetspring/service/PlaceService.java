package org.tp1ex2.projetspring.service;

import org.tp1ex2.projetspring.model.Place;
import java.util.List;

public interface PlaceService {
    Place createPlace(Place place);
    List<Place> getAllPlaces();
    Place getPlaceById(Long id);
    Place updatePlace(Place place);
    void deletePlace(Long id);
}
