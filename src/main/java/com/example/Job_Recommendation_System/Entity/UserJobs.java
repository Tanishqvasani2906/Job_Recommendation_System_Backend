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
    @Column(name = "title",nullable = false)
    private String title;
    @Column(nullable = false)
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
}
