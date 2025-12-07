package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.*;
import org.tp1ex2.projetspring.service.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private GuideService guideService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private TourService tourService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private AdminService adminService;

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("usersCount", userService.getAllUsers().size());
        model.addAttribute("placesCount", placeService.getAllPlaces().size());
        model.addAttribute("toursCount", tourService.getAllTours().size());
        model.addAttribute("reservationsCount", reservationService.getAllReservations().size());
        return "admin/dashboard";
    }

    // ===== GUIDES =====
    @GetMapping("/guides")
    public String listGuides(Model model) {
        List<Guide> guides = guideService.getAllGuides();
        model.addAttribute("guides", guides);
        return "admin/guides/list";
    }

    @GetMapping("/guides/add")
    public String addGuide(Model model) {
        model.addAttribute("guide", new Guide());
        return "admin/guides/add";
    }

    @PostMapping("/guides/save")
    public String saveGuide(@ModelAttribute Guide guide) {
        guideService.createGuide(guide);
        return "redirect:/admin/guides";
    }

    @GetMapping("/guides/edit/{id}")
    public String editGuide(@PathVariable Long id, Model model) {
        Guide guide = guideService.getGuideById(id);
        model.addAttribute("guide", guide);
        return "admin/guides/edit";
    }

    @PostMapping("/guides/update/{id}")
    public String updateGuide(@PathVariable Long id, @ModelAttribute Guide guide) {
        guide.setId(id);
        guideService.updateGuide(guide);
        return "redirect:/admin/guides";
    }

    @GetMapping("/guides/delete/{id}")
    public String deleteGuide(@PathVariable Long id) {
        guideService.deleteGuide(id);
        return "redirect:/admin/guides";
    }

    // ===== TOURS =====
    @GetMapping("/tours")
    public String listTours(Model model) {
        List<Tour> tours = tourService.getAllTours();
        model.addAttribute("tours", tours);
        return "admin/tours/list";
    }

    @GetMapping("/tours/add")
    public String addTour(Model model) {
        model.addAttribute("tour", new Tour());
        model.addAttribute("guides", guideService.getAllGuides());
        return "admin/tours/add";
    }

    @PostMapping("/tours/save")
    public String saveTour(@ModelAttribute Tour tour) {
        tourService.createTour(tour);
        return "redirect:/admin/tours";
    }

    @GetMapping("/tours/edit/{id}")
    public String editTour(@PathVariable Long id, Model model) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("tour", tour);
        model.addAttribute("guides", guideService.getAllGuides());
        return "admin/tours/edit";
    }

    @PostMapping("/tours/update/{id}")
    public String updateTour(@PathVariable Long id, @ModelAttribute Tour tour) {
        tour.setId(id);
        tourService.updateTour(tour);
        return "redirect:/admin/tours";
    }

    @GetMapping("/tours/delete/{id}")
    public String deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
        return "redirect:/admin/tours";
    }

    // ===== RESERVATIONS =====
    @GetMapping("/reservations")
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "admin/reservations/list";
    }

    @GetMapping("/reservations/add")
    public String addReservation(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("tours", tourService.getAllTours());
        return "admin/reservations/add";
    }

    @PostMapping("/reservations/save")
    public String saveReservation(@ModelAttribute Reservation reservation) {
        reservationService.createReservation(reservation);
        return "redirect:/admin/reservations";
    }

    @GetMapping("/reservations/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("tours", tourService.getAllTours());
        return "admin/reservations/edit";
    }

    @PostMapping("/reservations/update/{id}")
    public String updateReservation(@PathVariable Long id, @ModelAttribute Reservation reservation) {
        reservation.setId(id);
        reservationService.updateReservation(reservation);
        return "redirect:/admin/reservations";
    }

    @GetMapping("/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/admin/reservations";
    }

    // ===== REVIEWS =====
    @GetMapping("/reviews")
    public String listReviews(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "admin/reviews/list";
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/admin/reviews";
    }

    // ===== FAVORITES =====
    @GetMapping("/favorites")
    public String listFavorites(Model model) {
        List<Favorite> favorites = favoriteService.getAllFavorites();
        model.addAttribute("favorites", favorites);
        return "admin/favorites/list";
    }

    @GetMapping("/favorites/delete/{id}")
    public String deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return "redirect:/admin/favorites";
    }
}
