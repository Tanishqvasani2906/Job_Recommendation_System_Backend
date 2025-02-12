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
    private JobRepo jobRepo;
    @Autowired
    private UserJobsRepo userJobsRepo;

    public UserJobs saveNewJobs(UserJobs userJobs) {
        return userJobsRepo.save(userJobs);
    }

    public List<UserJobs> searchJobs(String title, String tags, String companyName, String city, String state) {
        return userJobsRepo.findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrStateContainingIgnoreCase(title, tags, companyName, city,state);
    }

}
