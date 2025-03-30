package com.example.Job_Recommendation_System.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "education")
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String education_id;

    @OneToMany(mappedBy = "education", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Degree> degrees;

    @OneToOne(mappedBy = "education", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Class10 class10;  // ✅ Fixed: Uses Class10 type

    @OneToOne(mappedBy = "education", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Class12 class12;  // ✅ Fixed: Uses Class12 type

    @OneToOne(mappedBy = "education", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private CareerPreferences careerPreferences;

    // Getters and Setters
    public CareerPreferences getCareerPreferences() {
        return careerPreferences;
    }

    public void setCareerPreferences(CareerPreferences careerPreferences) {
        this.careerPreferences = careerPreferences;
    }

    public Class10 getClass10() {
        return class10;
    }

    public void setClass10(Class10 class10) {
        this.class10 = class10;
    }

    public Class12 getClass12() {
        return class12;
    }

    public void setClass12(Class12 class12) {
        this.class12 = class12;
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

    public String getEducation_id() {
        return education_id;
    }

    public void setEducation_id(String education_id) {
        this.education_id = education_id;
    }
}
