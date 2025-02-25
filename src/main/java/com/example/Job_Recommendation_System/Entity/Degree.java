package com.example.Job_Recommendation_System.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "degree")
@AllArgsConstructor
@NoArgsConstructor
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String degree_id;
    private String courseName;
    private String degreeName;
    private String CGPA;
    private LocalDate courseDurationFrom;
    private LocalDate courseDurationTo;

    @ManyToOne
    @JoinColumn(name = "education_id")
    @JsonIgnore
    private Education education;

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public String getCGPA() {
        return CGPA;
    }

    public void setCGPA(String CGPA) {
        this.CGPA = CGPA;
    }

    public LocalDate getCourseDurationFrom() {
        return courseDurationFrom;
    }

    public void setCourseDurationFrom(LocalDate courseDurationFrom) {
        this.courseDurationFrom = courseDurationFrom;
    }

    public LocalDate getCourseDurationTo() {
        return courseDurationTo;
    }

    public void setCourseDurationTo(LocalDate courseDurationTo) {
        this.courseDurationTo = courseDurationTo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDegree_id() {
        return degree_id;
    }

    public void setDegree_id(String degree_id) {
        this.degree_id = degree_id;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }
}
