package com.monsalon.sportapp.services;

import com.monsalon.sportapp.entities.User;
import com.monsalon.sportapp.repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtTokenService {

    private static final String SECRET_KEY = "mySuperSecretKeyForJWTGeneration12345";
    private static final long EXPIRATION_TIME = 10 * 60 * 60 * 1000; // 10 heures
    private final Set<String> invalidatedTokens = new HashSet<>();
    private final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    private final UserRepository userRepository;

    @Autowired
    public JwtTokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * ✅ Générer un token JWT et mettre à jour l'utilisateur.
     */
    public String generateToken(String email) {
        String newToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();

        // ✅ Invalider l'ancien token et mettre à jour l'utilisateur
        Optional<User> userOptional = userRepository.findByEmail(email);
        userOptional.ifPresent(user -> {
            if (user.getLatestToken() != null) {
                invalidateToken(user.getLatestToken());
            }
            user.setLatestToken(newToken);
            userRepository.save(user);
        });

        return newToken;
    }

    /**
     * ✅ Valider un token JWT.
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);

            return !claimsJws.getBody().getExpiration().before(new Date())
                    && !isTokenInvalidated(token);
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * ✅ Extraire l'email depuis un token.
     */
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * ✅ Invalider un token.
     */
    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    /**
     * ✅ Vérifier si un token est invalidé.
     */
    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }

    public Optional<String> getLatestToken(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            System.out.println("📌 Token enregistré en base pour " + email + " : " + user.get().getLatestToken());
        } else {
            System.out.println("🚨 Aucun utilisateur trouvé pour " + email);
        }
        return user.map(User::getLatestToken);
    }
}
