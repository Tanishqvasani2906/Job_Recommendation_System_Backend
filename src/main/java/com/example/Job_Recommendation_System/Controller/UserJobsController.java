package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.UserJobs;
import com.example.Job_Recommendation_System.Service.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/userjobs")
public class UserJobsController {
    @Autowired
    private JobsService jobsService;

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COMPANY')")
    public ResponseEntity<UserJobs> postUserJobs(@RequestBody UserJobs userJobs) {
        // Get the current authenticated user's roles
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // You can also check for specific roles here, if needed
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isCompany = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_COMPANY"));

        if (isAdmin || isCompany) {
            // Proceed if the user has the correct role
            UserJobs userJobs1 = jobsService.saveNewJobs(userJobs);
            return ResponseEntity.ok(userJobs1);
        } else {
            // Handle unauthorized access case if needed
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/search-jobs")
    public ResponseEntity<List<UserJobs>> searchRelatedJobs(@RequestParam(required = false) String tags , @RequestParam(required = false) String title
    , @RequestParam(required = false) String companyName, @RequestParam(required = false) String city,
      @RequestParam(required = false) String state) {
        List<UserJobs> jobsList =  jobsService.searchJobs(title, tags, companyName, city,state);
        return ResponseEntity.ok(jobsList);
    }

}
