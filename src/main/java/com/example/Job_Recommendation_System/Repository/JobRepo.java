package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.Jobs;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface JobRepo extends JpaRepository<Jobs, String> {

    Jobs findByUrl(String url);
}
