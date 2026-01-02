package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.*;
import org.tp1ex2.projetspring.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);

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

    // Admin Login Page
    @GetMapping("/login")
    public String adminLoginPage(HttpSession session, Model model) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin != null) {
            return "redirect:/admin/dashboard";
        }
        return "admin/login";
    }

    // Admin Login Process
    @PostMapping("/login-process")
    public String adminLoginProcess(@RequestParam("login") String login, 
                                    @RequestParam("password") String password,
                                    Model model, HttpSession session) {
        try {
            logger.info("Admin login attempt for: " + login);
            Admin admin = adminService.findByLogin(login);
            
            if (admin != null && admin.getPassword() != null && admin.getPassword().equals(password)) {
                logger.info("Admin login successful: " + login);
                session.setAttribute("loggedInAdmin", admin);
                return "redirect:/admin/dashboard";
            } else {
                logger.warn("Invalid admin credentials for: " + login);
                model.addAttribute("error", "Invalid login or password");
                return "admin/login";
            }
        } catch (Exception e) {
            logger.error("Admin login failed", e);
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "admin/login";
        }
    }

    // Admin Logout
    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.removeAttribute("loggedInAdmin");
        return "redirect:/admin/login";
    }

    // Check if admin is logged in
    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInAdmin") != null;
    }

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            logger.warn("Unauthorized access attempt to admin dashboard");
            return "redirect:/admin/login";
        }
        
        model.addAttribute("usersCount", userService.getAllUsers().size());
        model.addAttribute("placesCount", placeService.getAllPlaces().size());
        model.addAttribute("toursCount", tourService.getAllTours().size());
        model.addAttribute("reservationsCount", reservationService.getAllReservations().size());
        model.addAttribute("reviewsCount", reviewService.getAllReviews().size());
        model.addAttribute("favoritesCount", favoriteService.getAllFavorites().size());
        model.addAttribute("guidesCount", guideService.getAllGuides().size());
        return "admin/dashboard";
    }
    
    // Redirect root admin to dashboard
    @GetMapping("")
    public String adminRoot(HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        return "redirect:/admin/dashboard";
    }

    // ===== GUIDES =====
    @GetMapping("/guides")
    public String listGuides(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        List<Guide> guides = guideService.getAllGuides();
        model.addAttribute("guides", guides);
        return "admin/guides/list";
    }

    @GetMapping("/guides/add")
    public String addGuide(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        model.addAttribute("guide", new Guide());
        return "admin/guides/add";
    }

    @PostMapping("/guides/save")
    public String saveGuide(@ModelAttribute Guide guide, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        guideService.createGuide(guide);
        return "redirect:/admin/guides";
    }

    @GetMapping("/guides/edit/{id}")
    public String editGuide(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        Guide guide = guideService.getGuideById(id);
        model.addAttribute("guide", guide);
        return "admin/guides/edit";
    }

    @PostMapping("/guides/update/{id}")
    public String updateGuide(@PathVariable Long id, @ModelAttribute Guide guide, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        guide.setId(id);
        guideService.updateGuide(guide);
        return "redirect:/admin/guides";
    }

    @GetMapping("/guides/delete/{id}")
    public String deleteGuide(@PathVariable Long id, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        guideService.deleteGuide(id);
        return "redirect:/admin/guides";
    }

    // ===== TOURS =====
    @GetMapping("/tours")
    public String listTours(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        List<Tour> tours = tourService.getAllTours();
        model.addAttribute("tours", tours);
        return "admin/tours/list";
    }

    @GetMapping("/tours/add")
    public String addTour(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        model.addAttribute("tour", new Tour());
        model.addAttribute("guides", guideService.getAllGuides());
        return "admin/tours/add";
    }

    @PostMapping("/tours/save")
    public String saveTour(@ModelAttribute Tour tour, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        tourService.createTour(tour);
        return "redirect:/admin/tours";
    }

    @GetMapping("/tours/edit/{id}")
    public String editTour(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        Tour tour = tourService.getTourById(id);
        model.addAttribute("tour", tour);
        model.addAttribute("guides", guideService.getAllGuides());
        return "admin/tours/edit";
    }

    @PostMapping("/tours/update/{id}")
    public String updateTour(@PathVariable Long id, @ModelAttribute Tour tour, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        tour.setId(id);
        tourService.updateTour(tour);
        return "redirect:/admin/tours";
    }

    @GetMapping("/tours/delete/{id}")
    public String deleteTour(@PathVariable Long id, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        tourService.deleteTour(id);
        return "redirect:/admin/tours";
    }

    // ===== RESERVATIONS =====
    @GetMapping("/reservations")
    public String listReservations(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "admin/reservations/list";
    }

    @GetMapping("/reservations/add")
    public String addReservation(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("tours", tourService.getAllTours());
        return "admin/reservations/add";
    }

    @PostMapping("/reservations/save")
    public String saveReservation(@ModelAttribute Reservation reservation, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        reservationService.createReservation(reservation);
        return "redirect:/admin/reservations";
    }

    @GetMapping("/reservations/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        Reservation reservation = reservationService.getReservationById(id);
        model.addAttribute("reservation", reservation);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("tours", tourService.getAllTours());
        return "admin/reservations/edit";
    }

    @PostMapping("/reservations/update/{id}")
    public String updateReservation(@PathVariable Long id, @ModelAttribute Reservation reservation, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        reservation.setId(id);
        reservationService.updateReservation(reservation);
        return "redirect:/admin/reservations";
    }

    @GetMapping("/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        reservationService.deleteReservation(id);
        return "redirect:/admin/reservations";
    }

    // ===== REVIEWS =====
    @GetMapping("/reviews")
    public String listReviews(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "admin/reviews/list";
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        reviewService.deleteReview(id);
        return "redirect:/admin/reviews";
    }

    // ===== FAVORITES =====
    @GetMapping("/favorites")
    public String listFavorites(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        List<Favorite> favorites = favoriteService.getAllFavorites();
        model.addAttribute("favorites", favorites);
        return "admin/favorites/list";
    }

    @GetMapping("/favorites/delete/{id}")
    public String deleteFavorite(@PathVariable Long id, HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/admin/login";
        favoriteService.deleteFavorite(id);
        return "redirect:/admin/favorites";
    }
}
