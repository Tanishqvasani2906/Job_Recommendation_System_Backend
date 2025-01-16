package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Config.JwtAuthenticationFilter;
import com.example.Job_Recommendation_System.Dto.ChangePasswordRequest;
import com.example.Job_Recommendation_System.Dto.LoginRequest;
import com.example.Job_Recommendation_System.Dto.LoginResponse;
import com.example.Job_Recommendation_System.Entity.Users;
import com.example.Job_Recommendation_System.Repository.UserRepo;
import com.example.Job_Recommendation_System.Service.JWTService;
import com.example.Job_Recommendation_System.Service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.SignatureException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/userlogin")
public class AuthController {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users user) {
        // Check if username or email already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username is already taken.", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email is already registered.", HttpStatus.BAD_REQUEST);
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        // Find user by username or email
        Users user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
        );

        // Extract details from the Authentication object
        String userId = user.getUser_id().toString(); // Assuming `Users` entity has a `user_id` field
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"))
                .getAuthority(); // Assuming role is in authorities
        String email = user.getEmail(); // Assuming `Users` entity has an `email` field

        // Generate JWT token using JWTService
        String jwtToken = jwtService.generateToken(userId, role, email);

        // Return the token and user ID in the response
        return ResponseEntity.ok(new LoginResponse(jwtToken, user.getUser_id()));
    }

    private final Set<String> blacklistedTokens = new HashSet<>();
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid or missing token.");
        }

        String token = authorizationHeader.substring(7); // Extract the token
        blacklistedTokens.add(token); // Add to the blacklist

        return ResponseEntity.ok("Logged out successfully.");
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from Authorization header
            String token = authHeader.substring(7);

            // Use the extractEmail method to get the email
            String userEmail = jwtAuthenticationFilter.extractEmail(token);

            // Find the user by email
            Optional<Users> userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return "User not found.";
            }

            Users user = userOptional.get();

            // Validate old password
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                return "Old password is incorrect.";
            }

            // Check new password and confirm password match
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return "New password and confirm password do not match.";
            }

            // Update the password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return "Password changed successfully.";
        } catch (Exception e) {
            // Handle any token parsing or other exceptions
            return "Invalid token or unauthorized request.";
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestTempToken(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            userService.generateTempToken(email);
            return ResponseEntity.ok("Password reset link sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    @PostMapping("/reset-password/{user_id}/{temp_token}")
    public ResponseEntity<?> resetPassword(
            @PathVariable String user_id,       // Receive user_id from path variable
            @PathVariable String temp_token,    // Receive temp_token from path variable
            @RequestBody Map<String, String> request) {  // Receive new password in the request body

        String newPassword = request.get("new_password");

        try {
            boolean resetSuccessful = userService.resetPassword(user_id, temp_token, newPassword);
            if (resetSuccessful) {
                return ResponseEntity.ok("Password has been reset successfully.");
            } else {
                return ResponseEntity.badRequest().body("Error: Invalid user ID or token.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/login/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        // Redirect to the Google login page
        response.sendRedirect("/oauth2/authorization/google");
    }




}
