package com.monsalon.sportapp.filters;

import com.monsalon.sportapp.services.JwtTokenService;
import com.monsalon.sportapp.utils.JwtTokenUtil;
import com.monsalon.sportapp.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, JwtTokenService jwtTokenService, CustomUserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("🚀 Interception de la requête: " + requestURI);

        // ✅ Exclure les routes de login et register
        if (requestURI.startsWith("/api/auth/login") || requestURI.startsWith("/api/auth/register")) {
            System.out.println("✅ Route d'authentification détectée, pas de filtrage !");
            chain.doFilter(request, response);
            return;
        }

        // ✅ Extraction du token
        String token = jwtTokenUtil.extractTokenFromRequest(request);
        if (token == null || !jwtTokenUtil.validateToken(token)) {
            System.out.println("❌ Token invalide ou manquant !");
            sendUnauthorizedResponse(response, "Token invalide ou manquant.");
            return;
        }

        // ✅ Vérification du dernier token enregistré
        String email = jwtTokenUtil.extractEmail(token);
        Optional<String> latestToken = jwtTokenService.getLatestToken(email);

        System.out.println("🔍 Vérification du token pour l'utilisateur: " + email);
        System.out.println("🔹 Token envoyé: " + token);
        System.out.println("🔹 Dernier token en base: " + latestToken.orElse("Aucun"));

        if (latestToken.isEmpty() || !token.equals(latestToken.get())) {
            System.out.println("🚨 Token obsolète détecté !");
            sendUnauthorizedResponse(response, "Token obsolète. Veuillez vous reconnecter.");
            return;
        }

        // ✅ Authentification de l'utilisateur
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    /**
     * ✅ Méthode utilitaire pour envoyer une réponse JSON d'erreur
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}
