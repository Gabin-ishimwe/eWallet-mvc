package com.ewallet.userService.data;


import com.ewallet.userService.entities.Role;
import com.ewallet.userService.entities.User;
import com.ewallet.userService.exceptions.NotFoundException;
import com.ewallet.userService.repositories.RoleRepository;
import com.ewallet.userService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        seedUsersAndRole();
    }

    public void seedUsersAndRole() throws NotFoundException {

        Role roleUser = seedRole("USER");
        Role roleAdmin = seedRole("ADMIN");
        User user1 = seedUser(
                "John",
                "Doe",
                "john@gmail.com",
                "#Password123",
                "0787857046",
                List.of(roleUser)
        );
        User user2 = seedUser(
                "Sam",
                "Patrick",
                "patrick@gmail.com",
                "#Password123",
                "07878570346",
                List.of(roleUser)
        );
        User user3 = seedUser(
                "Jane",
                "Angel",
                "angel@gmail.com",
                "#Password567",
                "0787383734",
                List.of(roleUser, roleAdmin)
        );
        userRepository.save(user2);
    }


    public User seedUser(String firstName, String lastName, String email, String password, String contact, List<Role> roles
    ) {
        return userRepository.save(
                User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .contactNumber(contact)
                        .roles(roles)
                        .build()
        );
    }

    public Role seedRole(String name) {
        return roleRepository.save(
                Role.builder()
                        .name(name)
                        .build()
        );
    }

}

