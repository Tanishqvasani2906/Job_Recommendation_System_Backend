package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.UserJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJobsRepo extends JpaRepository<UserJobs, String> {
//    List<UserJobs> findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrStateContainingIgnoreCase(
//        String title, String tags, String companyName, String city, String state);

    List<UserJobs> findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseAndCityContainingIgnoreCaseOrStateContainingIgnoreCase(
            String value1, String value1Again, String value1Third, String value2, String value2Again);
    List<UserJobs> findByCityContainingIgnoreCaseOrStateContainingIgnoreCase(
            String value2, String value2Again);
}
