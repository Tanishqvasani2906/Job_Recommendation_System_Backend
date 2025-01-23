package com.example.Job_Recommendation_System.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SelfPingService {

    private final RestTemplate restTemplate;

    @Value("${server.url}") // Replace with your server's base URL
    private String serverUrl;

    public SelfPingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 900000) // 15 minutes in milliseconds
    public void sendSelfPing() {
        try {
            String pingEndpoint = serverUrl + "/api/jobs/ping";
            String response = restTemplate.getForObject(pingEndpoint, String.class);
            System.out.println("Self-ping successful: " + response);
        } catch (Exception e) {
            System.err.println("Failed to ping the server: " + e.getMessage());
        }
    }
}

