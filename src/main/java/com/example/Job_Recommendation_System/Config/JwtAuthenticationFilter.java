package com.example.Job_Recommendation_System.Config;

import com.example.Job_Recommendation_System.Entity.Role;
import com.example.Job_Recommendation_System.Service.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.DecodingException;
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

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final String SECRET_KEY = "fd893e3258f7bbabbb32d2faf7a522ad68658e44d0ea1e1f0a586f4f0766598931ffd28306878037b787321e599088090eddd36f4a2d0921bcadbb87945e44e55181e6595ac59976d9a5370e9db23299cb7dfb386cfc593d9eb327f24b9505ce90e96157da52eff9a878073f34a54b825f0b3414e1530f047bcebeb93889a300b5c585471ee0d05a8efe8f1a8e153e45e82d6b8091e7a75cb00f409fafec22b0777060113436fcf7a8c3c0ec66b875a7f2960730990f3f3e8d798f1c298ccfd75037ce1fa3cd3fecd2c742151fe9e2ee168b709d1600169954933a147b6ad6dc10b4f9776bb0064279155684d2a3b0074dc3651e5bc7493630ca4ea9080a46b7"; // Replace with a secure key

    public JwtAuthenticationFilter(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String email = extractEmail(token);
                String tokenRole = extractRole(token);

                // Normalize role
                Role userRole = RoleUtils.normalizeRole(tokenRole);

                System.out.println("Extracted Email: " + email);
                System.out.println("Extracted Role: " + userRole);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    if (validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (DecodingException | SignatureException e) {
                System.err.println("Invalid or malformed token: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Error in role normalization: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error during token validation: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    private String extractRole(String token) {
        Claims claims = extractClaims(token);
        return (String) claims.get("role");
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (DecodingException | SignatureException e) {
            throw new DecodingException("Error decoding token: " + e.getMessage());
        }
    }

    private boolean validateToken(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new java.util.Date());
    }
}
