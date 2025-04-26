package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.JobApplication;
import com.example.Job_Recommendation_System.Entity.UserJobs;
import com.example.Job_Recommendation_System.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplication,String> {
    // Get all applications submitted by a specific user
    List<JobApplication> findByUser(Users user);

    // Get all applications for a specific job
    List<JobApplication> findByJob(UserJobs job);
}
