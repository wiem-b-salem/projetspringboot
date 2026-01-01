package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tp1ex2.projetspring.model.User;
import org.tp1ex2.projetspring.model.Guide;
import org.tp1ex2.projetspring.service.UserService;
import org.tp1ex2.projetspring.service.PlaceService;
import org.tp1ex2.projetspring.service.TourService;
import org.tp1ex2.projetspring.service.FavoriteService;
import org.tp1ex2.projetspring.service.ReservationService;
import org.tp1ex2.projetspring.service.ReviewService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private PlaceService placeService;
    
    @Autowired
    private TourService tourService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("listPlaces", placeService.getAll());
        model.addAttribute("tours", tourService.getAll());
        model.addAttribute("isLoggedIn", loggedInUser != null);
        model.addAttribute("user", loggedInUser);
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/login-process")
    public String loginProcess(@RequestParam("email") String email, @RequestParam("password") String password, 
                              Model model, HttpSession session) {
        try {
            logger.info("Login attempt for email: " + email);
            User user = userService.findByEmail(email);
            logger.info("User found: " + (user != null));
            if (user != null && user.getPassword() != null) {
                // Check password - supports both hashed (new users) and plain text (old users for backward compatibility)
                logger.info("Checking password for user: " + email + ", stored password length: " + user.getPassword().length());
                logger.info("Stored password starts with: " + (user.getPassword().length() > 2 ? user.getPassword().substring(0, Math.min(10, user.getPassword().length())) : "N/A"));
                boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
                logger.info("Password match result: " + passwordMatches);
                if (passwordMatches) {
                    logger.info("Password matched for user: " + user.getEmail());
                    session.setAttribute("loggedInUser", user);
                    model.addAttribute("success", "Welcome back, " + user.getFirstName() + "!");
                    return "redirect:/account";
                } else {
                    logger.warn("Password did not match for user: " + email);
                }
            }
            logger.warn("Invalid email or password for: " + email);
            model.addAttribute("error", "Invalid email or password");
            return "login";
        } catch (Exception e) {
            logger.error("Login failed", e);
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
    }

    @PostMapping("/signup-process")
    public String signupProcess(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                               @RequestParam("email") String email, @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                               @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,
                               @RequestParam("type") String type, Model model, HttpSession session) {
        try {
            logger.info("Signup attempt for: " + email + " as type: " + type);
            
            if (!password.equals(confirmPassword)) {
                logger.warn("Passwords do not match for: " + email);
                model.addAttribute("error", "Passwords do not match");
                return "signup";
            }

            User existingUser = userService.findByEmail(email);
            if (existingUser != null) {
                logger.warn("Email already registered: " + email);
                model.addAttribute("error", "Email already registered");
                return "signup";
            }

            User newUser;
            if ("GUIDE".equals(type)) {
                logger.info("Creating Guide user: " + email);
                newUser = new Guide();
            } else {
                logger.info("Creating Client user: " + email);
                newUser = new User();
            }
            
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setEmail(email);
            newUser.setPhoneNumber(phoneNumber != null && !phoneNumber.trim().isEmpty() ? phoneNumber : null);
            newUser.setPassword(passwordEncoder.encode(password)); // Encode password like REST API
            newUser.setType(type);
            
            logger.info("Saving user: " + email);
            User createdUser = userService.create(newUser);
            logger.info("User created successfully: " + createdUser.getId());
            
            session.setAttribute("loggedInUser", createdUser);
            return "redirect:/account";
        } catch (Exception e) {
            logger.error("Signup failed", e);
            model.addAttribute("error", "Signup failed: " + e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        return "redirect:/";
    }

    @GetMapping("/account")
    public String userAccount(HttpSession session, Model model) {
        try {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null) {
                logger.warn("No logged in user in session");
                return "redirect:/login";
            }

            logger.info("User accessing account: " + loggedInUser.getId());

            model.addAttribute("user", loggedInUser);
            
            // Get only this user's favorites, reservations, and reviews
            logger.info("Fetching favorites for user: " + loggedInUser.getId());
            model.addAttribute("favorites", favoriteService.getAll().stream()
                    .filter(f -> f.getUser() != null && f.getUser().getId().equals(loggedInUser.getId()))
                    .toList());
            
            logger.info("Fetching reservations for user: " + loggedInUser.getId());
            model.addAttribute("reservations", reservationService.getAll().stream()
                    .filter(r -> r.getUser() != null && r.getUser().getId().equals(loggedInUser.getId()))
                    .toList());
            
            logger.info("Fetching reviews for user: " + loggedInUser.getId());
            model.addAttribute("reviews", reviewService.getAll().stream()
                    .filter(r -> r.getUser() != null && r.getUser().getId().equals(loggedInUser.getId()))
                    .toList());
            
            logger.info("Account page rendered successfully for user: " + loggedInUser.getId());
            return "account";
        } catch (Exception e) {
            logger.error("Error accessing account page", e);
            return "error";
        }
    }

    @PostMapping("/account/update")
    public String updateAccount(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                               @RequestParam("email") String email, @RequestParam("phoneNumber") String phoneNumber,
                               @RequestParam(value = "password", required = false) String password, 
                               HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            loggedInUser.setFirstName(firstName);
            loggedInUser.setLastName(lastName);
            loggedInUser.setEmail(email);
            loggedInUser.setPhoneNumber(phoneNumber);
            
            if (password != null && !password.isEmpty()) {
                loggedInUser.setPassword(passwordEncoder.encode(password)); // Encode password when updating
            }
            
            User updatedUser = userService.update(loggedInUser);
            session.setAttribute("loggedInUser", updatedUser);
            model.addAttribute("success", "Profile updated successfully!");
            
            return "redirect:/account";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update profile: " + e.getMessage());
            return "redirect:/account";
        }
    }

    @PostMapping("/account/favorite/delete")
    public String removeFavorite(@RequestParam("favoriteId") Long favoriteId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            favoriteService.delete(favoriteId);
        } catch (Exception e) {
            // Error handling
        }
        
        return "redirect:/account";
    }

    @PostMapping("/account/reservation/cancel")
    public String cancelReservation(@RequestParam("reservationId") Long reservationId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            reservationService.delete(reservationId);
        } catch (Exception e) {
            // Error handling
        }
        
        return "redirect:/account";
    }

    @PostMapping("/account/review/delete")
    public String deleteReview(@RequestParam("reviewId") Long reviewId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            reviewService.delete(reviewId);
        } catch (Exception e) {
            // Error handling
        }
        
        return "redirect:/account";
    }
}
