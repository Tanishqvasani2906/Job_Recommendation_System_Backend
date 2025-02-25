package com.example.Job_Recommendation_System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int job_id;
    @Column(name = "title")
    private String title;
    @Column(name = "category")
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
