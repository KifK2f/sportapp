package com.monsalon.sportapp.controllers;

import com.monsalon.sportapp.dto.ChangePasswordRequest;
import com.monsalon.sportapp.entities.Role;
import com.monsalon.sportapp.entities.User;
import com.monsalon.sportapp.services.JwtTokenService;
import com.monsalon.sportapp.services.UserService;
import com.monsalon.sportapp.services.RoleService;
import com.monsalon.sportapp.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthController(UserService userService, RoleService roleService,
                          AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    // ✅ 1️⃣ Inscription d'un nouvel utilisateur
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists!");
        }

        // ✅ Vérifier si le rôle "USER" existe en base
        Optional<Role> userRole = roleService.findByName("USER");
        if (userRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Role USER not found in database!");
        }

        // ✅ Assigner le rôle "USER" existant
        user.setRoles(Collections.singleton(userRole.get()));
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encoder le mot de passe
        userService.saveUser(user); // Sauvegarder l'utilisateur

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    // ✅ 2️⃣ Connexion et génération du JWT
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        Optional<User> existingUser = userService.findByEmail(loginRequest.getEmail());

        if (existingUser.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), existingUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        User user = existingUser.get();

        // ✅ Ajouter l'authentification manquante ici ⬇️
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), loginRequest.getPassword());
        authenticationManager.authenticate(authenticationToken);

        // ✅ Générer un nouveau token
        String newToken = jwtTokenUtil.generateToken(user.getEmail());
        System.out.println("🚀 Nouveau token généré: " + newToken);

        // ✅ Vérifier si l'utilisateur a déjà un token stocké
        if (user.getLatestToken() != null) {
            jwtTokenService.invalidateToken(user.getLatestToken());
        }

        // ✅ Enregistrer le nouveau token
        user.setLatestToken(newToken);
        userService.saveUser(user);

        return ResponseEntity.ok(Collections.singletonMap("token", "Bearer " + newToken));
    }



    // ✅ 3️⃣ Déconnexion (Clear Security Context)
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext(); // Supprimer l'authentification de l'utilisateur
        return ResponseEntity.ok("Successfully logged out!");
    }

    // ✅ 1️⃣ Endpoint pour changer de mot de passe
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        // Récupérer l'email de l'utilisateur connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean success = userService.changePassword(email, request.getOldPassword(), request.getNewPassword());

        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ancien mot de passe incorrect !");
        }

        return ResponseEntity.ok("Mot de passe changé avec succès !");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser, @RequestHeader("Authorization") String token) {
        // Récupération de l'email depuis le token
        String email = jwtTokenUtil.extractEmail(token.substring(7));

        // Vérifier si l'utilisateur existe
        Optional<User> existingUser = userService.findByEmail(email);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = existingUser.get();

        // Mise à jour des informations (ne pas modifier l'email)
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Chiffrement du mot de passe
        }

        userService.saveUser(user);

        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {
        // Récupération de l'email depuis le token
        String email = jwtTokenUtil.extractEmail(token.substring(7));

        // Vérifier si l'utilisateur existe
        Optional<User> existingUser = userService.findByEmail(email);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userService.deleteUser(existingUser.get().getId());

        return ResponseEntity.ok("User deleted successfully");
    }

}
