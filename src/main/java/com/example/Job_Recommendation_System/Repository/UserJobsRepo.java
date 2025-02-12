package com.example.Job_Recommendation_System.Repository;

import com.example.Job_Recommendation_System.Entity.UserJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJobsRepo extends JpaRepository<UserJobs, String> {
//    @Query("SELECT j FROM UserJobs j WHERE " +
//            "(:title IS NULL OR j.title ILIKE CONCAT('%', :title, '%')) " +
//            "AND (:tags IS NULL OR j.tags ILIKE CONCAT('%', :tags, '%')) " +
//            "AND (:companyName IS NULL OR j.companyName ILIKE CONCAT('%', :companyName, '%')) " +
//            "AND (:city IS NULL OR j.city ILIKE CONCAT('%', :city, '%')) " +
//            "AND (:state IS NULL OR j.state ILIKE CONCAT('%', :state, '%'))")
//    List<UserJobs> searchJobs(
//            @Param("title") String title,
//            @Param("tags") String tags,
//            @Param("companyName") String companyName,
//            @Param("city") String city,
//            @Param("state") String state);
    List<UserJobs> findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrStateContainingIgnoreCase(
        String title, String tags, String companyName, String city, String state);


}
