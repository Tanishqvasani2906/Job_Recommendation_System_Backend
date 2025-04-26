package com.example.Job_Recommendation_System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_job_id", referencedColumnName = "userJobId", nullable = false)
    private UserJobs job;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    @Column(columnDefinition = "TEXT") private String techStack;
    @Column(columnDefinition = "TEXT") private String github;
    @Column(columnDefinition = "TEXT") private String projects;
    @Column(columnDefinition = "TEXT") private String experienceYears;
    @Column(columnDefinition = "TEXT") private String certifications;
    @Column(columnDefinition = "TEXT") private String roleInterest;
    @Column(columnDefinition = "TEXT") private String problemSolving;
    @Column(columnDefinition = "TEXT") private String teamworkExperience;
    @Column(columnDefinition = "TEXT") private String remoteSetup;
    @Column(columnDefinition = "TEXT") private String salaryExpectation;
    @Column(columnDefinition = "TEXT") private String learningGoal;
    @Column(columnDefinition = "TEXT") private String careerGoals;
    @Column(columnDefinition = "TEXT") private String openSource;

    @Column(nullable = false)
    private String status = "PENDING";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(columnDefinition = "TEXT")
    private String customAnswers;

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

    public String getCareerGoals() {
        return careerGoals;
    }

    public void setCareerGoals(String careerGoals) {
        this.careerGoals = careerGoals;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getCustomAnswers() {
        return customAnswers;
    }

    public void setCustomAnswers(String customAnswers) {
        this.customAnswers = customAnswers;
    }

    public String getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(String experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public UserJobs getJob() {
        return job;
    }

    public void setJob(UserJobs job) {
        this.job = job;
    }

    public String getLearningGoal() {
        return learningGoal;
    }

    public void setLearningGoal(String learningGoal) {
        this.learningGoal = learningGoal;
    }

    public String getOpenSource() {
        return openSource;
    }

    public void setOpenSource(String openSource) {
        this.openSource = openSource;
    }

    public String getProblemSolving() {
        return problemSolving;
    }

    public void setProblemSolving(String problemSolving) {
        this.problemSolving = problemSolving;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getRemoteSetup() {
        return remoteSetup;
    }

    public void setRemoteSetup(String remoteSetup) {
        this.remoteSetup = remoteSetup;
    }

    public String getRoleInterest() {
        return roleInterest;
    }

    public void setRoleInterest(String roleInterest) {
        this.roleInterest = roleInterest;
    }

    public String getSalaryExpectation() {
        return salaryExpectation;
    }

    public void setSalaryExpectation(String salaryExpectation) {
        this.salaryExpectation = salaryExpectation;
    }

    public String getTeamworkExperience() {
        return teamworkExperience;
    }

    public void setTeamworkExperience(String teamworkExperience) {
        this.teamworkExperience = teamworkExperience;
    }

    public String getTechStack() {
        return techStack;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

}
