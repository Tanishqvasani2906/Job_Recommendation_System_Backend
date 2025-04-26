package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Dto.JobApplicationRequestDTO;
import com.example.Job_Recommendation_System.Dto.JobApplicationResponseDTO;
import com.example.Job_Recommendation_System.Service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {
    @Autowired
    private JobApplicationService jobApplicationService;

    @PostMapping("/quick-apply")
    public ResponseEntity<JobApplicationResponseDTO> applyToJob(@RequestBody JobApplicationRequestDTO applicationRequestDTO) {
        JobApplicationResponseDTO response = jobApplicationService.applyToJob(applicationRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getApplicationsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(jobApplicationService.getApplicationsByUser(userId));
    }

    // 🟡 Get all applications for a specific job (for moderators/admins)
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getApplicationsForJob(@PathVariable String jobId) {
        return ResponseEntity.ok(jobApplicationService.getApplicationsForJob(jobId));
    }

    // 🔴 Optional: Get specific application by application ID
    @GetMapping("/{applicationId}")
    public ResponseEntity<JobApplicationResponseDTO> getApplicationById(@PathVariable String applicationId) {
        return ResponseEntity.ok(jobApplicationService.getApplicationById(applicationId));
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<JobApplicationResponseDTO> updateStatus(
            @PathVariable String applicationId,
            @RequestParam String status
    ) {
        JobApplicationResponseDTO responseDTO = jobApplicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(responseDTO);
    }
}
