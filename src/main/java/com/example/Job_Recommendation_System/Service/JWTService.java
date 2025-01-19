
package com.example.Job_Recommendation_System.Service;


import com.example.Job_Recommendation_System.Entity.Users;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JWTService {

    private static final String SECRET_KEY = "fd893e3258f7bbabbb32d2faf7a522ad68658e44d0ea1e1f0a586f4f0766598931ffd28306878037b787321e599088090eddd36f4a2d0921bcadbb87945e44e55181e6595ac59976d9a5370e9db23299cb7dfb386cfc593d9eb327f24b9505ce90e96157da52eff9a878073f34a54b825f0b3414e1530f047bcebeb93889a300b5c585471ee0d05a8efe8f1a8e153e45e82d6b8091e7a75cb00f409fafec22b0777060113436fcf7a8c3c0ec66b875a7f2960730990f3f3e8d798f1c298ccfd75037ce1fa3cd3fecd2c742151fe9e2ee168b709d1600169954933a147b6ad6dc10b4f9776bb0064279155684d2a3b0074dc3651e5bc7493630ca4ea9080a46b7"; // Use a secure key
    private static final long EXPIRATION_TIME = 84 * 24 * 60 * 60 * 1000L; // 84 days in milliseconds

    // Generate a JWT token
//    public String generateToken(String userId, String role, String email) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("user_id", userId);
//        claims.put("role", role);
//        claims.put("email", email);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(email)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }

    // Validate the JWT token
//    public boolean validateToken(String token) {
//        try {
//            // Validate if the token is expired or not
//            return !isTokenExpired(token);
//        } catch (JwtException | IllegalArgumentException e) {
//            return false; // Invalid or expired token
//        }
//    }

    private Map<String, Date> blacklistedTokens = new ConcurrentHashMap<>();

    public String generateToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        // Store the raw role name without 'ROLE_' prefix
        claims.put("roles", user.getRole().name());  // "COMPANY" or "ADMIN"
        claims.put("user_id", user.getUser_id());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // Subject as the email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            System.err.println("Error during token validation: " + e.getMessage());
            return false;
        }
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractUserId(String token) {
        return extractClaims(token).get("user_id", String.class);
    }

    public String extractRole(String token) {
        // Extract the raw role (e.g., "COMPANY", "ADMIN")
        return extractClaims(token).get("roles", String.class);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new IllegalArgumentException("Invalid JWT signature: " + e.getMessage(), e);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new IllegalArgumentException("JWT token is expired: " + e.getMessage(), e);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new IllegalArgumentException("Malformed JWT token: " + e.getMessage(), e);
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            throw new IllegalArgumentException("Unsupported JWT token: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing JWT token: " + e.getMessage(), e);
        }
    }

    public void blacklistToken(String token) {
        Date expiration = extractExpiration(token);
        blacklistedTokens.put(token, expiration);
    }

    public boolean isTokenBlacklisted(String token) {
        Date expiration = blacklistedTokens.get(token);
        if (expiration != null) {
            if (expiration.before(new Date())) {
                blacklistedTokens.remove(token);
                return false;
            }
            return true;
        }
        return false;
    }

}
