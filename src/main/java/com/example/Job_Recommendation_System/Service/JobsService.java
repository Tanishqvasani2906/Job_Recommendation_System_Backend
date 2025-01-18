package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Repository.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobsService {
    @Autowired
    private JobRepo jobRepo;
}
