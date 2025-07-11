package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.Jobs;
import com.example.Job_Recommendation_System.Repository.JobRepo;
import com.example.Job_Recommendation_System.Service.JobUpdateService;
import com.example.Job_Recommendation_System.Service.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobsController {
    @Autowired
    private JobsService jobsService;
    @Autowired
    private JobRepo jobRepo;
    @Autowired
    private JobUpdateService jobUpdateService;

//    @Cacheable(value = "jobsCache")
//    @GetMapping(produces = "application/json")
//    public ResponseEntity<List<Jobs>> getAllJobs() {
//        List<Jobs> list = jobRepo.findAll();
//        return ResponseEntity.ok(list);
//    }
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<Jobs>> getAllJobsPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<Jobs> jobPage = jobRepo.findAll(pageable);
        return ResponseEntity.ok(jobPage);
    }

    @GetMapping("/getRelatedJobs")
    public ResponseEntity<List<Jobs>> getAllJobs(@RequestParam List<String> tags) {
        // Convert ["java", "php"] → "java|php"
        String regexTags = String.join("|", tags).toLowerCase();
        List<Jobs> jobsList = jobRepo.findByMatchingTags(regexTags);
        return ResponseEntity.ok(jobsList);
    }


    @PostMapping("/insert")
    public void insertJobs(@RequestBody List<Jobs> jobs) {
        jobUpdateService.insertJobs(jobs);  // Calling the service method to insert jobs
    }

    @CacheEvict(value = "jobsCache", allEntries = true) // Clears cache
    @GetMapping("/update-jobs")
    public ResponseEntity<String> updateJobsManually() {
        jobUpdateService.updateJobs();
        return ResponseEntity.ok("Job update triggered");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Server is alive!");
    }

}