package com.example.Job_Recommendation_System.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "internships")
@AllArgsConstructor
@NoArgsConstructor
public class Internships {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String internship_id;
    @Column(name = "company_name")
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    private String companyName;
    @Column(name = "duration_from")
    private LocalDate durationFrom;
    @Column(name = "duration_to")
    private LocalDate durationTo;
    @Column(name = "description")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    // Ensure start date is before end date
    @PrePersist
    @PreUpdate
    public void validateDates() {
        if (durationFrom != null && durationTo != null && durationFrom.isAfter(durationTo)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDurationFrom() {
        return durationFrom;
    }

    public void setDurationFrom(LocalDate durationFrom) {
        this.durationFrom = durationFrom;
    }

    public LocalDate getDurationTo() {
        return durationTo;
    }

    public void setDurationTo(LocalDate durationTo) {
        this.durationTo = durationTo;
    }

    public String getInternship_id() {
        return internship_id;
    }

    public void setInternship_id(String internship_id) {
        this.internship_id = internship_id;
    }
}
