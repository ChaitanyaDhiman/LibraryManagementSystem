package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //Display all user list
    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    //Show form for adding a new user
    @GetMapping("/new") // Handles GET requests to /users/new
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Added new user");
        return "add-edit-user";
    }

    //Handles form submission for adding/updating a user.
    @PostMapping("/save") //Handles POST request to /users/save
    public String saveUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            if (user.getId() == null) {
                userService.createUser(user);
                redirectAttributes.addFlashAttribute("message", "User added successfully");
            } else {
                userService.updateUser(user.getId(), user);
                redirectAttributes.addFlashAttribute("message", "User updated successfully");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            if(user.getId() == null) {
                return "redirect:/users/new";
            } else {
                return "redirect:/users/edit/" + user.getId();
            }
        }
        return "redirect:/users";
    }
    // Show form for editing an existing member
    @GetMapping("/edit/{id}") // Handles GET requests to /members/edit/{id}
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User with id " + id + " not found") );
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit user (ID: " + id + ")");
            return "add-edit-user";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users";
        }
    }

    //Delete a user.
    @GetMapping("/delete/{id}") // Handles GET requests to /members/delete/{id}
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }

}
