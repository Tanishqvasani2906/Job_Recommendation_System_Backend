package com.example.Job_Recommendation_System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_jobs")
@AllArgsConstructor
@NoArgsConstructor
public class UserJobs {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userJobId;
    @Column(name = "title",nullable = false,columnDefinition = "TEXT")
    private String title;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String companyName;
    @Column(columnDefinition = "TEXT", nullable = false) // For long descriptions
    private String description;
    @Column(nullable = false)
    private String contactEmail;
    @Column(name = "tags",columnDefinition = "TEXT" )
    private String tags;
    @Column(nullable = false)
    private String companyUrl;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "city", nullable = false, columnDefinition = "TEXT")
    private String city;
    @Column(name = "state", nullable = false, columnDefinition = "TEXT")
    private String state;
    @Column(name = "job_type" , nullable = false)
    private String jobType;
    @Column(name = "salary", nullable = false)
    private String salary;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @Column private boolean askTechStack;
    @Column private boolean askGitHub;
    @Column private boolean askProjects;
    @Column private boolean askYearsExperience;
    @Column private boolean askCertifications;
    @Column private boolean askRoleInterest;
    @Column private boolean askProblemSolving;
    @Column private boolean askTeamworkExperience;
    @Column private boolean askRemoteSetup;
    @Column private boolean askSalaryExpectation;
    @Column private boolean askLearningGoal;
    @Column private boolean askCareerGoals;
    @Column private boolean askOpenSourceContribution;


    public Users getUser() {
        return users;
    }

    public void setUser(Users users) {
        this.users = users;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserJobId() {
        return userJobId;
    }

    public void setUserJobId(String userJobId) {
        this.userJobId = userJobId;
    }
    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public boolean isAskCareerGoals() {
        return askCareerGoals;
    }

    public void setAskCareerGoals(boolean askCareerGoals) {
        this.askCareerGoals = askCareerGoals;
    }

    public boolean isAskCertifications() {
        return askCertifications;
    }

    public void setAskCertifications(boolean askCertifications) {
        this.askCertifications = askCertifications;
    }

    public boolean isAskGitHub() {
        return askGitHub;
    }

    public void setAskGitHub(boolean askGitHub) {
        this.askGitHub = askGitHub;
    }

    public boolean isAskLearningGoal() {
        return askLearningGoal;
    }

    public void setAskLearningGoal(boolean askLearningGoal) {
        this.askLearningGoal = askLearningGoal;
    }

    public boolean isAskOpenSourceContribution() {
        return askOpenSourceContribution;
    }

    public void setAskOpenSourceContribution(boolean askOpenSourceContribution) {
        this.askOpenSourceContribution = askOpenSourceContribution;
    }

    public boolean isAskProblemSolving() {
        return askProblemSolving;
    }

    public void setAskProblemSolving(boolean askProblemSolving) {
        this.askProblemSolving = askProblemSolving;
    }

    public boolean isAskProjects() {
        return askProjects;
    }

    public void setAskProjects(boolean askProjects) {
        this.askProjects = askProjects;
    }

    public boolean isAskRemoteSetup() {
        return askRemoteSetup;
    }

    public void setAskRemoteSetup(boolean askRemoteSetup) {
        this.askRemoteSetup = askRemoteSetup;
    }

    public boolean isAskRoleInterest() {
        return askRoleInterest;
    }

    public void setAskRoleInterest(boolean askRoleInterest) {
        this.askRoleInterest = askRoleInterest;
    }

    public boolean isAskSalaryExpectation() {
        return askSalaryExpectation;
    }

    public void setAskSalaryExpectation(boolean askSalaryExpectation) {
        this.askSalaryExpectation = askSalaryExpectation;
    }

    public boolean isAskTeamworkExperience() {
        return askTeamworkExperience;
    }

    public void setAskTeamworkExperience(boolean askTeamworkExperience) {
        this.askTeamworkExperience = askTeamworkExperience;
    }

    public boolean isAskTechStack() {
        return askTechStack;
    }

    public void setAskTechStack(boolean askTechStack) {
        this.askTechStack = askTechStack;
    }

    public boolean isAskYearsExperience() {
        return askYearsExperience;
    }

    public void setAskYearsExperience(boolean askYearsExperience) {
        this.askYearsExperience = askYearsExperience;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
