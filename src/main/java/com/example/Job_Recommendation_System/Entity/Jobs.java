package com.example.Job_Recommendation_System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Entity
@Table(name = "jobs")
@AllArgsConstructor
@NoArgsConstructor
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String jobId;
    @Column(name = "title",columnDefinition = "TEXT")
    private String title;
    @Column(name = "company",columnDefinition = "TEXT")
    private String company;
    @Column(name = "url",columnDefinition = "TEXT")
    private String url;
    @Column(name = "tags",columnDefinition = "TEXT" )
    private String tags;
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;
    @Column(name = "category",columnDefinition = "TEXT")
    private String category;
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
