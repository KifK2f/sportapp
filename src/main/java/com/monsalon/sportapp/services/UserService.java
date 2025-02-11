package com.monsalon.sportapp.services;

import com.monsalon.sportapp.entities.User;
import com.monsalon.sportapp.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.monsalon.sportapp.services.JwtTokenService;


import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // ✅ Méthode pour modifier le mot de passe
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Vérifier si l'ancien mot de passe correspond
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                return false; // Ancien mot de passe incorrect
            }

            // Modifier le mot de passe et l'enregistrer
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }

        return false;
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // ✅ Mettre à jour le dernier token
    public void updateLatestToken(String email, String newToken) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        userOptional.ifPresent(user -> {
            user.setLatestToken(newToken);
            userRepository.save(user);
        });
    }

}
