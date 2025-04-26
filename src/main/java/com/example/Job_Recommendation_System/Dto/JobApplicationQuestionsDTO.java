package com.example.Job_Recommendation_System.Dto;

import java.util.Map;

public class JobApplicationQuestionsDTO {
//    private String jobId;
//    private Map<String, String> questions; // key = fieldName, value = question
    private String techStack;
    private String github;
    private String projects;
    private String experienceYears;
    private String certifications;
    private String roleInterest;
    private String problemSolving;
    private String teamworkExperience;
    private String remoteSetup;
    private String salaryExpectation;
    private String learningGoal;
    private String careerGoals;
    private String openSource;

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



    public String getLearningGoal() {
        return learningGoal;
    }

    public void setLearningGoal(String learningGoal) {
        this.learningGoal = learningGoal;
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
}
