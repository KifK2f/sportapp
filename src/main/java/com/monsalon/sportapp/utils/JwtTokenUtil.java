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
    private static final long EXPIRATION_TIME = 10 * 60 * 60 * 1000; // 10 heures
    private final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final Set<String> invalidatedTokens = new HashSet<>();

    /**
     * ✅ Générer un token JWT
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
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
            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (JwtException e) {
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
            return header.substring(7);
        }
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
