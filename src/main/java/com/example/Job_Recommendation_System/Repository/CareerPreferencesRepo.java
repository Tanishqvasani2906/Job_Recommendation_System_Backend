package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.CareerPreferences;
import com.example.Job_Recommendation_System.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CareerPreferencesRepo extends JpaRepository<CareerPreferences, String> {
    Optional<CareerPreferences> findByUsers(Users user);
    @Query("SELECT cp FROM CareerPreferences cp LEFT JOIN FETCH cp.education WHERE cp.users = :user")
    Optional<CareerPreferences> findByUsersWithEducation(@Param("user") Users user);

    @Query("""
    SELECT cp FROM CareerPreferences cp 
    LEFT JOIN FETCH cp.internships 
    LEFT JOIN FETCH cp.education 
    WHERE cp.users = :user
""")
    Optional<CareerPreferences> findByUsersWithEducationAndInternships(@Param("user") Users user);

}
