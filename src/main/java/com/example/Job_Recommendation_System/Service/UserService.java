package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Entity.Users;
import com.example.Job_Recommendation_System.Repository.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String secretKey = "fd893e3258f7bbabbb32d2faf7a522ad68658e44d0ea1e1f0a586f4f0766598931ffd28306878037b787321e599088090eddd36f4a2d0921bcadbb87945e44e55181e6595ac59976d9a5370e9db23299cb7dfb386cfc593d9eb327f24b9505ce90e96157da52eff9a878073f34a54b825f0b3414e1530f047bcebeb93889a300b5c585471ee0d05a8efe8f1a8e153e45e82d6b8091e7a75cb00f409fafec22b0777060113436fcf7a8c3c0ec66b875a7f2960730990f3f3e8d798f1c298ccfd75037ce1fa3cd3fecd2c742151fe9e2ee168b709d1600169954933a147b6ad6dc10b4f9776bb0064279155684d2a3b0074dc3651e5bc7493630ca4ea9080a46b7";

    public String generateTempToken(String email) throws MessagingException, IOException {
        Optional<Users> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Users user = userOptional.get();

        // Generate a temporary token
        String tempToken = Jwts.builder()
                .setSubject(user.getUser_id())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 minutes
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // Save the token and its expiry time in the user record
        user.setTempToken(tempToken);
        user.setTempTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepo.save(user);

        // Construct the email link
        String resetLink = "https://careervistaa.vercel.app/reset-password/" + user.getUser_id() + "/" + tempToken;

        // Send the email
        sendEmail(user.getEmail(), "Password Reset Request", resetLink);

        return tempToken;
    }

//    private void sendEmail(String to, String subject, String body) throws MessagingException, UnsupportedEncodingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setFrom("vasanitanishq2906@gmail.com","Career Vistaa");
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText("<p>Click the link below to reset your password:</p><a href=\"" + body + "\">Reset Password</a>", true);
//        mailSender.send(message);
//    }
private void sendEmail(String to, String subject, String resetLink) throws MessagingException, IOException, UnsupportedEncodingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setFrom("vasanitanishq2906@gmail.com", "Career Vista");
    helper.setTo(to);
    helper.setSubject(subject);

    // Firebase Storage logo URL (Public Link)
    String imageUrl = "https://firebasestorage.googleapis.com/v0/b/disaster-management-2906.appspot.com/o/images%2F942762.png?alt=media";

    // Load the email template
    String htmlTemplate = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/reset-password.html")), StandardCharsets.UTF_8);

    // Replace placeholders with actual values
    String emailContent = htmlTemplate
            .replace("{{ reset_link }}", resetLink)
            .replace("{{ image_url }}", imageUrl);

    // Set email body as HTML
    helper.setText(emailContent, true);

    mailSender.send(message);
}


    public boolean resetPassword(String userId, String tempToken, String newPassword) {
        Optional<Users> userOptional = userRepo.findByUserId(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Users user = userOptional.get();

        // Validate the tempToken
        if (!tempToken.equals(user.getTempToken())) {
            throw new RuntimeException("Invalid or expired token");
        }

        // Check if the token has expired
        if (user.getTempTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        // Set the new password (ensure password is hashed)
        user.setPassword(passwordEncoder.encode(newPassword));  // Remember to hash the password before saving it
        user.setTempToken(null);         // Clear the tempToken after successful password reset
        user.setTempTokenExpiry(null);   // Clear the token expiration
        userRepo.save(user);             // Save the updated user data

        return true;
    }




}
