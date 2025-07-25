package com.example.librarymanagementsystem.services;

import com.example.librarymanagementsystem.model.Role;
import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.repositories.RoleRepository;
import com.example.librarymanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //Get all users.
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //Get user by its ID.
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    //Add a new user.
    public User createUser(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this Email: " + user.getEmail() + " already exists");
        }
        Role defaultRole = roleRepository.findByRole("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getEmail());
        newUser.setRoles(Collections.singletonList(defaultRole));
        return userRepository.save(newUser);
    }

    //Update an existing user.
    public User updateUser(Long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            User userToUpdate = userOptional.get();
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setEmail(user.getEmail());
            return userRepository.save(userToUpdate);
        } else {
            throw new IllegalArgumentException("User with this Id: " + id + " does not exist");
        }
    }

    // Delete a member by ID
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
    }
}
