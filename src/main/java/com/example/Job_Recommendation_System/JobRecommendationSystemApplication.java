package com.example.Job_Recommendation_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobRecommendationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobRecommendationSystemApplication.class, args);
	}

}
