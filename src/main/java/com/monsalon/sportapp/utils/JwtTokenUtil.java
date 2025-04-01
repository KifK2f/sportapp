package com.monsalon.sportapp.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "mySuperSecretKeyForJWTGeneration12345";
    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 jours
    private final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final Set<String> invalidatedTokens = new HashSet<>();

    /**
     * ✅ Générer un token JWT
     */
    public String generateToken(String email) {
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("✅ JWT généré: " + token);
        return token;
    }

    /**
     * ✅ Vérifier la validité du token
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);

            System.out.println("✅ Token valide !");
            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (JwtException e) {
            System.out.println("❌ Erreur validation JWT: " + e.getMessage());
            return false;
        }
    }

    /**
     * ✅ Extraire l'email depuis un token
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
     * ✅ Extraire le token depuis la requête HTTP
     */
    public String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            System.out.println("✅ Token extrait: " + header.substring(7));
            return header.substring(7);
        }
        System.out.println("❌ Aucun token trouvé dans la requête");
        return null;
    }

    /**
     * ✅ Invalider un token JWT (utilisé lors de la déconnexion).
     * @param token Le token à invalider.
     */
    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }
}
