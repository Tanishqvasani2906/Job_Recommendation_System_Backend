package com.example.Job_Recommendation_System.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobRequestDTO {
    private String title;
    private String companyName;
    private String description;
    private String contactEmail;
    private String tags;
    private String companyUrl;
    private String city;
    private String state;
    private String jobType;
    private String salary;

    private boolean askTechStack;
    private boolean askGitHub;
    private boolean askProjects;
    private boolean askYearsExperience;
    private boolean askCertifications;
    private boolean askRoleInterest;
    private boolean askProblemSolving;
    private boolean askTeamworkExperience;
    private boolean askRemoteSetup;
    private boolean askSalaryExpectation;
    private boolean askLearningGoal;
    private boolean askCareerGoals;
    private boolean askOpenSourceContribution;

    public String getCity() {
        return city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public String getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public String getJobType() {
        return jobType;
    }

    public String getSalary() {
        return salary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isAskYearsExperience() {
        return askYearsExperience;
    }

    public void setAskYearsExperience(boolean askYearsExperience) {
        this.askYearsExperience = askYearsExperience;
    }

    public boolean isAskTechStack() {
        return askTechStack;
    }

    public void setAskTechStack(boolean askTechStack) {
        this.askTechStack = askTechStack;
    }

    public boolean isAskTeamworkExperience() {
        return askTeamworkExperience;
    }

    public void setAskTeamworkExperience(boolean askTeamworkExperience) {
        this.askTeamworkExperience = askTeamworkExperience;
    }

    public boolean isAskSalaryExpectation() {
        return askSalaryExpectation;
    }

    public void setAskSalaryExpectation(boolean askSalaryExpectation) {
        this.askSalaryExpectation = askSalaryExpectation;
    }

    public boolean isAskRoleInterest() {
        return askRoleInterest;
    }

    public void setAskRoleInterest(boolean askRoleInterest) {
        this.askRoleInterest = askRoleInterest;
    }

    public boolean isAskRemoteSetup() {
        return askRemoteSetup;
    }

    public void setAskRemoteSetup(boolean askRemoteSetup) {
        this.askRemoteSetup = askRemoteSetup;
    }

    public boolean isAskProjects() {
        return askProjects;
    }

    public void setAskProjects(boolean askProjects) {
        this.askProjects = askProjects;
    }

    public boolean isAskProblemSolving() {
        return askProblemSolving;
    }

    public void setAskProblemSolving(boolean askProblemSolving) {
        this.askProblemSolving = askProblemSolving;
    }

    public boolean isAskOpenSourceContribution() {
        return askOpenSourceContribution;
    }

    public void setAskOpenSourceContribution(boolean askOpenSourceContribution) {
        this.askOpenSourceContribution = askOpenSourceContribution;
    }

    public boolean isAskLearningGoal() {
        return askLearningGoal;
    }

    public void setAskLearningGoal(boolean askLearningGoal) {
        this.askLearningGoal = askLearningGoal;
    }

    public boolean isAskGitHub() {
        return askGitHub;
    }

    public void setAskGitHub(boolean askGitHub) {
        this.askGitHub = askGitHub;
    }

    public boolean isAskCertifications() {
        return askCertifications;
    }

    public void setAskCertifications(boolean askCertifications) {
        this.askCertifications = askCertifications;
    }

    public boolean isAskCareerGoals() {
        return askCareerGoals;
    }

    public void setAskCareerGoals(boolean askCareerGoals) {
        this.askCareerGoals = askCareerGoals;
    }
}

