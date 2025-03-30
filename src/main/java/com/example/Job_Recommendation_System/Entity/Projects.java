package com.example.Job_Recommendation_System.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@AllArgsConstructor
@NoArgsConstructor
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String projects_id;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "project_description")
    private String projectDescription;
//    @Column(name = "project_duration")
//    private String projectDuration;
    @Column(name = "project_duration_from")
    private LocalDate projectDurationFrom;

    @Column(name = "project_duration_to")
    private LocalDate projectDurationTo;


    @ManyToOne
    @JoinColumn(name = "careerPreferences_id")
    @JsonIgnore
    private CareerPreferences careerPreferences;

    public CareerPreferences getCareerPreferences() {
        return careerPreferences;
    }

    public void setCareerPreferences(CareerPreferences careerPreferences) {
        this.careerPreferences = careerPreferences;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public LocalDate getProjectDurationFrom() {
        return projectDurationFrom;
    }

    public void setProjectDurationFrom(LocalDate projectDurationFrom) {
        this.projectDurationFrom = projectDurationFrom;
    }

    public LocalDate getProjectDurationTo() {
        return projectDurationTo;
    }

    public void setProjectDurationTo(LocalDate projectDurationTo) {
        this.projectDurationTo = projectDurationTo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjects_id() {
        return projects_id;
    }

    public void setProjects_id(String projects_id) {
        this.projects_id = projects_id;
    }
}
