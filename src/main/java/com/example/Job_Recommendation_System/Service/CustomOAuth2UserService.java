package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Entity.Users;
import com.example.Job_Recommendation_System.Repository.UserRepo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepo userRepo;

    public CustomOAuth2UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract user information
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // Save or update the user in your database
        Users user = userRepo.findByEmail(email)
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setEmail(email);
                    newUser.setUsername(name);
                    newUser.setPassword("");  // No password for OAuth users
                    return userRepo.save(newUser);
                });

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, "email");
    }
}

