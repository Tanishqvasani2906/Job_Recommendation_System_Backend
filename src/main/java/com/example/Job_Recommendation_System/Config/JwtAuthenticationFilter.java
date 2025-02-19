package com.example.Job_Recommendation_System.Config;

import com.example.Job_Recommendation_System.Entity.Role;
import com.example.Job_Recommendation_System.Service.CustomUserDetailsService;

import com.example.Job_Recommendation_System.Service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.DecodingException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final String SECRET_KEY = "fd893e3258f7bbabbb32d2faf7a522ad68658e44d0ea1e1f0a586f4f0766598931ffd28306878037b787321e599088090eddd36f4a2d0921bcadbb87945e44e55181e6595ac59976d9a5370e9db23299cb7dfb386cfc593d9eb327f24b9505ce90e96157da52eff9a878073f34a54b825f0b3414e1530f047bcebeb93889a300b5c585471ee0d05a8efe8f1a8e153e45e82d6b8091e7a75cb00f409fafec22b0777060113436fcf7a8c3c0ec66b875a7f2960730990f3f3e8d798f1c298ccfd75037ce1fa3cd3fecd2c742151fe9e2ee168b709d1600169954933a147b6ad6dc10b4f9776bb0064279155684d2a3b0074dc3651e5bc7493630ca4ea9080a46b7"; // Replace with a secure key

    public JwtAuthenticationFilter(CustomUserDetailsService userDetailsService,JWTService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = extractTokenFromRequest(request);

        if (jwtToken != null && jwtService.validateToken(jwtToken)) {
            String userEmail = jwtService.extractEmail(jwtToken);
            String userId = jwtService.extractUserId(jwtToken);
            String role = jwtService.extractRole(jwtToken);

            // Load user details (You can customize this as needed)
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (userDetails != null) {
                // Create authentication object with authorities
                List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );

                // Set authentication to the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    // Extract JWT token from Authorization header (Bearer <token>)
    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public String extractEmail(String token) {
        return jwtService.extractClaims(token).getSubject();
    }

    private String extractRole(String token) {
        Claims claims = jwtService.extractClaims(token);
        // Ensure the role is correctly extracted with the "ROLE_" prefix
        return (String) claims.get("roles"); // Assuming the role key is "roles"
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.get("email", String.class);
        System.out.println("Extracted Email from Token: " + email);

        if (email == null) {
            throw new IllegalArgumentException("Email extracted from JWT is null!");
        }

        return email;
    }



}