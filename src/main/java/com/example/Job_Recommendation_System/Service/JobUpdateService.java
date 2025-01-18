package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Entity.Jobs;
import com.example.Job_Recommendation_System.Repository.JobRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobUpdateService {
    @Autowired
    private JobRepo jobRepo;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${remotive.api.url}")
    private String remotiveApiUrl;

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void updateJobs() {
        try {
            String jobsData = restTemplate.getForObject(remotiveApiUrl, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jobs = mapper.readTree(jobsData).get("jobs");

            List<Jobs> jobList = new ArrayList<>();
            for (JsonNode jobNode : jobs) {
                Jobs job = new Jobs();
                job.setTitle(jobNode.get("title").asText());
                job.setCompany(jobNode.get("company_name").asText());
                job.setUrl(jobNode.get("url").asText());
                job.setTags(jobNode.get("tags").toString());
                job.setDescription(jobNode.get("description").asText());
                job.setCategory(jobNode.get("category").asText());
                job.setUpdatedAt(LocalDateTime.now());
                jobList.add(job);
            }
            jobRepo.saveAll(jobList);
        } catch (Exception e) {
            System.err.println("Failed to update jobs: " + e.getMessage());
        }
    }

    @Transactional
    public void insertJobs(List<Jobs> jobs) {
        jobRepo.saveAll(jobs); // Insert jobs in a batch
    }
}
