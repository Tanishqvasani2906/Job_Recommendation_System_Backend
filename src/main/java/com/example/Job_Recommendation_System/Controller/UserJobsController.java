package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Dto.JobRequestDTO;
import com.example.Job_Recommendation_System.Entity.UserJobs;
import com.example.Job_Recommendation_System.Entity.Users;
import com.example.Job_Recommendation_System.Repository.JobRepo;
import com.example.Job_Recommendation_System.Repository.UserJobsRepo;
import com.example.Job_Recommendation_System.Repository.UserRepo;
import com.example.Job_Recommendation_System.Service.JobsService;
import com.example.Job_Recommendation_System.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userjobs")
@CrossOrigin("http://127.0.0.1:5173")
public class UserJobsController {
    @Autowired
    private JobsService jobsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    //    @PostMapping("/new")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COMPANY')")
//    public ResponseEntity<UserJobs> postUserJobs(@RequestBody UserJobs userJobs) {
//        // Get the current authenticated user's roles
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        // You can also check for specific roles here, if needed
//        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//        boolean isCompany = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_COMPANY"));
//
//        if (isAdmin || isCompany) {
//            // Proceed if the user has the correct role
//            UserJobs userJobs1 = jobsService.saveNewJobs(userJobs);
//            return ResponseEntity.ok(userJobs1);
//        } else {
//            // Handle unauthorized access case if needed
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
//    }
        @PostMapping("/new")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COMPANY')")
        public ResponseEntity<UserJobs> postUserJobs(@RequestBody JobRequestDTO jobRequest) {
            // Get the current authenticated user's details
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String loginIdentifier = authentication.getName(); // This could be email or username

            // Check if user has the correct role
            boolean isAdmin = authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            boolean isCompany = authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_COMPANY"));

            if (!isAdmin && !isCompany) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Find user by email or username
            Optional<Users> currentUserOpt = userRepo.findByEmail(loginIdentifier);
            if (currentUserOpt.isEmpty()) {
                currentUserOpt = userRepo.findByUsername(loginIdentifier);
            }

            Users currentUser = currentUserOpt.orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
            );

            // Create new job and associate it with the user
            UserJobs newJob = new UserJobs();
            newJob.setTitle(jobRequest.getTitle());
            newJob.setCompanyName(jobRequest.getCompanyName());
            newJob.setDescription(jobRequest.getDescription());
            newJob.setContactEmail(jobRequest.getContactEmail());
            newJob.setTags(jobRequest.getTags());
            newJob.setCompanyUrl(jobRequest.getCompanyUrl());
            newJob.setCity(jobRequest.getCity());
            newJob.setState(jobRequest.getState());
            newJob.setUpdatedAt(LocalDateTime.now());
            newJob.setJobType(jobRequest.getJobType());
            newJob.setSalary(jobRequest.getSalary());
            newJob.setUser(currentUser); // Set the user

            UserJobs savedJob = jobsService.saveNewJobs(newJob);
            return ResponseEntity.ok(savedJob);
        }

    @GetMapping("/search-jobs")
    public ResponseEntity<List<UserJobs>> searchRelatedJobs(
            @RequestParam(required = false) String value1,
            @RequestParam(required = false) String value2) {

        List<UserJobs> jobsList = jobsService.searchJobs(value1, value2);
        return ResponseEntity.ok(jobsList);
    }

    @GetMapping("/all-jobs-based-of-company/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY','ROLE_ADMIN')")
    public ResponseEntity<?> getJobsByCompany(@PathVariable String user_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isCompany = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_COMPANY"));

        if (!isAdmin && !isCompany) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not authenticated for this access");
        }

        List<UserJobs> jobsList = jobsService.getJobsByUserId(user_id);
        if (jobsList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No jobs are posted by this company");
        }
        return ResponseEntity.ok(jobsList);
    }





}
