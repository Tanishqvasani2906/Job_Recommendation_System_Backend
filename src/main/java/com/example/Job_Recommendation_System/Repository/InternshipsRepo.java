package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.Internships;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InternshipsRepo extends JpaRepository<Internships, String> {
    @Query("SELECT i FROM Internships i WHERE i.careerPreferences.careerPreferences_id = :careerPreferencesId")
    List<Internships> findByCareerPreferencesId(@Param("careerPreferencesId") String careerPreferencesId);}
