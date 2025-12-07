package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.Place;
import org.tp1ex2.projetspring.service.PlaceService;

import java.util.List;

@Controller
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping("/add")
    public String addPlace(Model model) {
        Place place = new Place();
        model.addAttribute("placeForm", place);
        return "addplace";
    }

    @PostMapping("/save")
    public String savePlace(@ModelAttribute("placeForm") Place place) {
        placeService.createPlace(place);
        return "redirect:/places/all";
    }

    @GetMapping("/all")
    public String listPlaces(Model model) {
        List<Place> listPlaces = placeService.getAllPlaces();
        model.addAttribute("listPlaces", listPlaces);
        return "listplaces";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Place place = placeService.getPlaceById(id);
        model.addAttribute("place", place);
        return "updateplace";
    }

    @PostMapping("/update/{id}")
    public String updatePlace(@PathVariable("id") Long id, Place place, BindingResult result, Model model) {
        if(result.hasErrors()) {
            place.setId(id);
            return "updateplace";
        }
        placeService.updatePlace(place);
        return "redirect:/places/all";
    }

    @GetMapping("/delete/{id}")
    public String deletePlace(@PathVariable("id") Long id) {
        placeService.deletePlace(id);
        return "redirect:/places/all";
    }
}
