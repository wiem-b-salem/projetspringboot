package org.tp1ex2.projetspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.tp1ex2.projetspring.model.User;
import org.tp1ex2.projetspring.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

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
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "updateuser";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, User user, BindingResult result, Model model) {
        if(result.hasErrors()) {
            user.setId(id);
            return "updateuser";
        }
        userService.updateUser(user);
        return "redirect:/users/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users/all";
    }
}
