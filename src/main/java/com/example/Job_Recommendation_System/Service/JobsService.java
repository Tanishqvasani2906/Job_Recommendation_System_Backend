package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Entity.Jobs;
import com.example.Job_Recommendation_System.Entity.UserJobs;
import com.example.Job_Recommendation_System.Repository.JobRepo;
import com.example.Job_Recommendation_System.Repository.UserJobsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobsService {
    @Autowired
    private UserJobsRepo userJobsRepo;

    public UserJobs saveNewJobs(UserJobs userJobs) {
        return userJobsRepo.save(userJobs);
    }

//    public List<UserJobs> searchJobs(String title, String tags, String companyName, String city, String state) {
//        return userJobsRepo.findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrStateContainingIgnoreCase(title, tags, companyName, city,state);
//    }
public List<UserJobs> searchJobs(String value1, String value2) {
    if ((value1 == null || value1.isEmpty()) && (value2 != null && !value2.isEmpty())) {
        // If value1 is empty but value2 is provided, search only by city/state
        return userJobsRepo.findByCityContainingIgnoreCaseOrStateContainingIgnoreCase(value2, value2);
    }
    // Otherwise, search with both value1 and value2
    return userJobsRepo.findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseAndCityContainingIgnoreCaseOrStateContainingIgnoreCase(
            value1, value1, value1, value2, value2);
}

    public List<UserJobs> getJobsByUserId(String user_id) {
        return userJobsRepo.findByUserId(user_id);
    }
}
