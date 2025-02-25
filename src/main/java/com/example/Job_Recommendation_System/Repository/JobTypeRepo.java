package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTypeRepo extends JpaRepository<JobType, Integer> {

}
