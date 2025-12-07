package org.tp1ex2.projetspring.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tp1ex2.projetspring.model.Place;
import org.tp1ex2.projetspring.repository.PlaceRepository;
import org.tp1ex2.projetspring.service.PlaceService;

import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Override
    public Place createPlace(Place place) {
        return placeRepository.save(place);
    }

    @Override
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    @Override
    public Place getPlaceById(Long id) {
        return placeRepository.findById(id).orElse(null);
    }

    @Override
    public Place updatePlace(Place place) {
        return placeRepository.saveAndFlush(place);
    }

    @Override
    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }
}
