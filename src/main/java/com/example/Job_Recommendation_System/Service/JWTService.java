package com.example.Job_Recommendation_System.Service;


import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JWTService {

    private static final String SECRET_KEY = "fd893e3258f7bbabbb32d2faf7a522ad68658e44d0ea1e1f0a586f4f0766598931ffd28306878037b787321e599088090eddd36f4a2d0921bcadbb87945e44e55181e6595ac59976d9a5370e9db23299cb7dfb386cfc593d9eb327f24b9505ce90e96157da52eff9a878073f34a54b825f0b3414e1530f047bcebeb93889a300b5c585471ee0d05a8efe8f1a8e153e45e82d6b8091e7a75cb00f409fafec22b0777060113436fcf7a8c3c0ec66b875a7f2960730990f3f3e8d798f1c298ccfd75037ce1fa3cd3fecd2c742151fe9e2ee168b709d1600169954933a147b6ad6dc10b4f9776bb0064279155684d2a3b0074dc3651e5bc7493630ca4ea9080a46b7"; // Use a secure key
    private static final long EXPIRATION_TIME = 84 * 24 * 60 * 60 * 1000L; // 84 days in milliseconds

    // Generate a JWT token
    public String generateToken(String userId, String role, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userId);
        claims.put("role", role);
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Validate the JWT token
//    public boolean validateToken(String token) {
//        try {
//            // Validate if the token is expired or not
//            return !isTokenExpired(token);
//        } catch (JwtException | IllegalArgumentException e) {
//            return false; // Invalid or expired token
//        }
//    }

    public boolean validateToken(String token) {
        try {
            // Preprocess the token to fix any URL-safe Base64 issues
            String fixedToken = token.replace('-', '+').replace('_', '/');

            // Add padding if necessary
            int missingPadding = 4 - (fixedToken.length() % 4);
            if (missingPadding > 0 && missingPadding < 4) {
                fixedToken += "=".repeat(missingPadding);
            }

            // Parse the token (replace `secretKey` with your actual secret key)
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(fixedToken);

            return true; // Token is valid
        } catch (Exception e) {
            System.err.println("Error during token validation: " + e.getMessage());
            return false; // Token is invalid
        }
    }



    // Method to extract the email from the token
    private String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Assuming the subject is the email
    }

    // Method to check if the token is expired
    private boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.before(new Date());
    }

    // Method to extract the expiration date from the token
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // Extract claims from the token
//    private Claims extractClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody();
//    }
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract user_id from the token
    public String extractUserId(String token) {
        return extractClaims(token).get("user_id", String.class);
    }

    // Extract role from the token
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
    private Set<String> blacklistedTokens = new HashSet<>();

    // Add the token to the blacklist
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Check if the token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
