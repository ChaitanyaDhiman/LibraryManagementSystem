package com.example.librarymanagementsystem.util;

import com.example.librarymanagementsystem.model.Role;
import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.repositories.RoleRepository;
import com.example.librarymanagementsystem.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        //Create role if they don't exist
        Role adminRole = roleRepository.findByRole("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role librarianRole = roleRepository.findByRole("ROLE_LIBRARIAN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_LIBRARIAN")));
        Role userRole = roleRepository.findByRole("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        //Create user if they don't exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User("admin", passwordEncoder.encode("admin"), "admin@email.com");
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminRoles.add(librarianRole);
            adminRoles.add(userRole);
            adminUser.setRoles(adminRoles);
            userRepository.save(adminUser);
        }
        if (userRepository.findByUsername("librarian").isEmpty()) {
            User librarianUser = new User("librarian", passwordEncoder.encode("password"), "librarian@email.com");
            Set<Role> librarianRoles = new HashSet<>();
            librarianRoles.add(librarianRole);
            librarianRoles.add(userRole);
            librarianUser.setRoles(librarianRoles);
            userRepository.save(librarianUser);
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            User regularUser = new User("user", passwordEncoder.encode("password"), "user@email.com");
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            regularUser.setRoles(userRoles);
            userRepository.save(regularUser);
        }
    }
}
