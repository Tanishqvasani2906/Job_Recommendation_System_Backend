package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.JobType;
import com.example.Job_Recommendation_System.Repository.JobTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobtype")
public class JobTypeController {
    @Autowired
    private JobTypeRepo jobTypeRepo;

    @GetMapping
    public List<JobType> getAllJobTypes() {
        return jobTypeRepo.findAll();
    }
    @PostMapping("/upload")
    public String uploadJobTypes(@RequestBody List<JobType> jobTypes) {
        jobTypeRepo.saveAll(jobTypes);
        return "Job types uploaded successfully!";
    }
}
