package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.Jobs;
import com.example.Job_Recommendation_System.Entity.UserJobs;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Jobs, String> {
    Jobs findByUrl(String url);

    @Query(value = "SELECT * FROM jobs j WHERE " +
            "LOWER(j.tags) SIMILAR TO CONCAT('%(', :tags, ')%')", nativeQuery = true)
    List<Jobs> findByMatchingTags(@Param("tags") String tags);
}
