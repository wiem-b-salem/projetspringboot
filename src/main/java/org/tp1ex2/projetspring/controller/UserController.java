package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.User;
import org.tp1ex2.projetspring.model.Guide;
import org.tp1ex2.projetspring.service.UserService;
import org.tp1ex2.projetspring.service.GuideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private GuideService guideService;

    @GetMapping("/add")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("userForm", user);
        return "adduser";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("userForm") User user) {
        userService.createUser(user);
        return "redirect:/users/all";
    }

    @GetMapping("/all")
    public String listUsers(Model model) {
        List<User> listUsers = userService.getAllUsers();
        model.addAttribute("listUsers", listUsers);
        return "listusers";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        try {
            User user = userService.getUserById(id);
            // Check if this is a guide - if so, redirect to guide edit page
            if (user != null && "GUIDE".equals(user.getType())) {
                return "redirect:/admin/guides/edit/" + id;
            }
            model.addAttribute("user", user);
            return "updateuser";
        } catch (Exception e) {
            logger.error("Error loading user for edit", e);
            model.addAttribute("error", "Error loading user");
            return "listusers";
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, User user, BindingResult result, Model model) {
        try {
            if(result.hasErrors()) {
                user.setId(id);
                return "updateuser";
            }
            
            // Prevent changing user type to GUIDE through this form
            User existing = userService.getUserById(id);
            if (existing != null && "GUIDE".equals(existing.getType())) {
                logger.warn("Attempted to edit guide through user form - redirecting to guide edit");
                return "redirect:/admin/guides/edit/" + id;
            }
            
            // Ensure they're not trying to change type to GUIDE
            if (user != null && "GUIDE".equals(user.getType())) {
                logger.warn("Attempted to change user type to GUIDE - preventing");
                user.setType("CLIENT");
            }
            
            user.setId(id);
            userService.updateUser(user);
            return "redirect:/users/all";
        } catch (Exception e) {
            logger.error("Error updating user", e);
            model.addAttribute("error", "Error updating user: " + e.getMessage());
            return "updateuser";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users/all";
    }
}
