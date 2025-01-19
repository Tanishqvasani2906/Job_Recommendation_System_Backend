package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.UserJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJobsRepo extends JpaRepository<UserJobs, String> {
}
