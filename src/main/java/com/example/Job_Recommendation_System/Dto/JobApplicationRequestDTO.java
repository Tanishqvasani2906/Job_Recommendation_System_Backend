package com.example.Job_Recommendation_System.Dto;

public class JobApplicationRequestDTO {
    private String userId;
    private String userJobId;
    private String message; // Optional field
    private JobApplicationQuestionsDTO answers;  // this will hold answers to custom questions


    public String getUserJobId() {
        return userJobId;
    }

    public void setUserJobId(String userJobId) {
        this.userJobId = userJobId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public JobApplicationQuestionsDTO getAnswers() {
        return answers;
    }

    public void setAnswers(JobApplicationQuestionsDTO answers) {
        this.answers = answers;
    }
}
