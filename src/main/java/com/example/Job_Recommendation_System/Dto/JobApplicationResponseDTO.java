package com.example.Job_Recommendation_System.Dto;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
//@Builder
//@AllArgsConstructor
@Transactional
public class JobApplicationResponseDTO {
    private String applicationId;
    private String jobTitle;
    private String companyName;
    private String applicantName;
    private String resumeUrl;
    private JobApplicationQuestionsDTO responses;
    private LocalDateTime appliedAt;
    private String status;

    public JobApplicationResponseDTO(String applicationId, String jobTitle, String companyName, String status, String applicantName) {
        this.applicationId = applicationId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.status = status;
        this.applicantName = applicantName;
    }

    public JobApplicationResponseDTO() {

    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public JobApplicationQuestionsDTO getResponses() {
        return responses;
    }

    public void setResponses(JobApplicationQuestionsDTO responses) {
        this.responses = responses;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
