package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectsRepo extends JpaRepository<Projects, String> {
    @Query("SELECT p FROM Projects p WHERE p.careerPreferences.careerPreferences_id = :careerPreferencesId")
    List<Projects> findByCareerPreferencesId(@Param("careerPreferencesId") String careerPreferencesId);
}

