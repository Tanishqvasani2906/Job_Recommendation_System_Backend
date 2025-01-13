package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, String> {
    Optional<Users> findByUsernameOrEmail(String username, String email);
    Optional<Users> findByUsername(String username); // Find by username
    Optional<Users> findByEmail(String email); // Find by email

    @Query("SELECT u FROM Users u WHERE u.user_id = :userId")
    Optional<Users> findByUserId(@Param("userId") String userId);

}
