package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Config.JwtAuthenticationFilter;
import com.example.Job_Recommendation_System.Dto.ChangePasswordRequest;
import com.example.Job_Recommendation_System.Dto.LoginRequest;
import com.example.Job_Recommendation_System.Dto.LoginResponse;
import com.example.Job_Recommendation_System.Entity.Users;
import com.example.Job_Recommendation_System.Repository.UserRepo;
import com.example.Job_Recommendation_System.Service.JWTService;
import com.example.Job_Recommendation_System.Service.TokenBlacklistService;
import com.example.Job_Recommendation_System.Service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.SignatureException;
import java.time.Duration;
import java.util.*;

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
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

@PostMapping("/register")
public ResponseEntity<Map<String, String>> registerUser(@RequestBody Users user) {
    Map<String, String> response = new HashMap<>();

    // Check if username or email already exists
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        response.put("error", "Username is already registered");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        response.put("error", "Email is already registered");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Encode the password before saving
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // Save the user
    userRepository.save(user);

    response.put("message", "User registered successfully!");
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

@PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
    // Find user by username or email
    Optional<Users> userOptional = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail());

    if (userOptional.isEmpty()) {
        // Return error if user is not found
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Username or email is not registered"));
    }

    Users user = userOptional.get();

    try {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
        );
    } catch (BadCredentialsException e) {
        // Return error if password is incorrect
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Your password is incorrect"));
    }

    // Generate JWT token using JWTService
    String jwtToken = jwtService.generateToken(user);

    // Return the token and user ID in the response
    return ResponseEntity.ok(new LoginResponse(jwtToken, user.getUser_id()));
}

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authorizationHeader,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid token"));
        }

        String token = authorizationHeader.substring(7);
        String email = jwtAuthenticationFilter.extractUsername(token);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid token payload"));
        }

        tokenBlacklistService.blacklistToken(token);

        // Invalidate session
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.setHeader("Clear-Site-Data", "\"cookies\", \"storage\", \"executionContexts\"");
        return ResponseEntity.ok(Collections.singletonMap("message", "Logged out successfully."));
    }

    @PostMapping("/change-password")
    public Map<String,String> changePassword(@RequestBody ChangePasswordRequest request, @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from Authorization header
            String token = authHeader.substring(7);

            // Use the extractEmail method to get the email
            String userEmail = jwtAuthenticationFilter.extractEmail(token);

            // Find the user by email
            Optional<Users> userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return Collections.singletonMap("error","User with this email not found");
            }

            Users user = userOptional.get();

            // Validate old password
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                return Collections.singletonMap("error","Old password is incorrect.");
            }

            // Check new password and confirm password match
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return Collections.singletonMap("error","New password and confirm password do not match.");
            }

            // Update the password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return Collections.singletonMap("error","Password changed successfully.");
        } catch (Exception e) {
            // Handle any token parsing or other exceptions
            return Collections.singletonMap("error","Invalid token or unauthorized request.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestTempToken(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            userService.generateTempToken(email);
            return ResponseEntity.ok(Collections.singletonMap("error","Password reset link sent to your email."));
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
                return ResponseEntity.ok(Collections.singletonMap("error","Password has been reset successfully."));
            } else {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error","Error: Invalid user ID or token."));
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

//    @GetMapping("/oauth2/callback/google")
//    public ResponseEntity<String> handleGoogleCallback(@RequestParam("code") String code) {
//        System.out.println("Authorization code received: " + code);
//        return ResponseEntity.ok("Authorization code received");
//    }
@GetMapping("/auth/google/callback")
public ResponseEntity<String> handleGoogleCallback(@RequestParam("code") String code) {
    try {
        // Step 1: Exchange authorization code for access token
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", "1066158005621-5ecafgah1sc3pfpmvorf1k5ehjvoouas.apps.googleusercontent.com");
        params.add("client_secret", "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");
        params.add("redirect_uri", "https://job-recommendation-system-backend.onrender.com/userlogin/auth/google/callback");
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, request, Map.class);

        // Step 2: Extract access token
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // Step 3: Fetch user info using the access token
        String userInfo = getUserInfo(accessToken);

        // Step 4: Parse and process user info (authenticate or register)
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userInfoJson = objectMapper.readTree(userInfo);
        System.out.println(userInfoJson);

        String googleId = userInfoJson.get("sub").asText(); // Google unique user ID
        String email = userInfoJson.get("email").asText();
        String name = userInfoJson.get("name").asText();

        // Check if the user exists in the database
        Optional<Users> existingUser = userRepository.findByEmail(email);
        Users user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // Register new user if not exists
            user = new Users();
//            user.setGoogleId(googleId);
            user.setEmail(email);
            user.setFirstName(name);
            userRepository.save(user);
        }

        // Step 5: Generate JWT token
        String jwtToken = jwtService.generateToken(user);

//        // Step 6: Redirect user to the frontend with the token
        String frontendUrl = "https://careervistaa.vercel.app"; // Replace with your frontend URL
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", frontendUrl + "/?token=" + jwtToken)
                .build();
//        return ResponseEntity.ok("User authenticated. Token: " + jwtToken);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during Google OAuth process");
    }
}
//@GetMapping("/auth/google/callback")
//public ResponseEntity<String> handleGoogleCallback(@RequestParam("code") String code) {
//    try {
//        // Step 1: Exchange authorization code for access token
//        RestTemplate restTemplate = new RestTemplate();
//        String tokenUrl = "https://oauth2.googleapis.com/token";
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("code", code);
//        params.add("client_id", "1066158005621-5ecafgah1sc3pfpmvorf1k5ehjvoouas.apps.googleusercontent.com");
//        params.add("client_secret", "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");
//        params.add("redirect_uri", "https://job-recommendation-system-backend.onrender.com/userlogin/auth/google/callback");
//        params.add("grant_type", "authorization_code");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, request, Map.class);
//
//        // Step 2: Extract access token
//        String accessToken = (String) tokenResponse.getBody().get("access_token");
//
//        // Step 3: Fetch user info using the access token
//        String userInfo = getUserInfo(accessToken);
//
//        // Step 4: Parse and process user info (authenticate or register)
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode userInfoJson = objectMapper.readTree(userInfo);
//
//        String googleId = userInfoJson.get("sub").asText(); // Google unique user ID
//        String email = userInfoJson.get("email").asText();
//        String name = userInfoJson.get("name").asText();
//
//        // Check if the user exists in the database
//        Optional<Users> existingUser = userRepository.findByEmail(email);
//        Users user;
//        if (existingUser.isPresent()) {
//            user = existingUser.get();
//        } else {
//            // Register new user if not exists
//            user = new Users();
////            user.setGoogleId(googleId);
//            user.setEmail(email);
//            user.setFirstName(name);
//            userRepository.save(user);
//        }
//
//        // Step 5: Generate JWT token
//        String jwtToken = jwtService.generateToken(user);
//
//        // Step 6: Set token in HTTP-Only Cookie
//        String frontendUrl = "https://careervistaa.vercel.app"; // Replace with your frontend URL
//        ResponseCookie jwtCookie = ResponseCookie.from("token", jwtToken)
//                .httpOnly(true) // Prevent JavaScript from accessing the cookie
//                .secure(true) // Ensure the cookie is sent over HTTPS
//                .path("/") // Accessible throughout the entire frontend
//                .maxAge(Duration.ofDays(7)) // Set expiration time
//                .sameSite("Strict") // Prevent CSRF attacks
//                .build();
//
//        // Redirect to the frontend
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("Location", frontendUrl);
//        responseHeaders.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
//
//        return ResponseEntity.status(HttpStatus.FOUND).headers(responseHeaders).build();
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during Google OAuth process");
//    }
//}



    public String getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    public void processUserInfo(String userInfoJson) {
        // Parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> userInfo = objectMapper.readValue(userInfoJson, Map.class);
            String googleId = (String) userInfo.get("sub");
            String name = (String) userInfo.get("name");
            String email = (String) userInfo.get("email");
            String picture = (String) userInfo.get("picture");

            // Check if user exists in the database
            Optional<Users> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                // Log in the user (generate JWT or session)
                System.out.println("User already exists: " + email);
            } else {
                // Register the user
                Users newUser = new Users();
//                newUser.setGoogleId(googleId);
                newUser.setFirstName(name);
                newUser.setEmail(email);
//                newUser.setProfilePicture(picture);
                userRepository.save(newUser);
                System.out.println("New user registered: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing user info");
        }
    }
}