package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DegreeRepo extends JpaRepository<Degree, String> {
    @Query("SELECT d FROM Degree d WHERE d.education.education_id = :educationId")
    List<Degree> findByEducationId(@Param("educationId") String educationId);

}
