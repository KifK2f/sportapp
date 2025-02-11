package com.monsalon.sportapp.services;

import com.monsalon.sportapp.entities.Role;
import com.monsalon.sportapp.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void initializeRoles() {
        if (!roleRepository.existsByName("USER")) {
            roleRepository.save(new Role("USER"));
        }
        if (!roleRepository.existsByName("ADMIN")) {
            roleRepository.save(new Role("ADMIN"));
        }
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
