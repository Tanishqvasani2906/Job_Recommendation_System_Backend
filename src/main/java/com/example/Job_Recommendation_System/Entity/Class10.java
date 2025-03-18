package com.example.Job_Recommendation_System.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "class10")
@AllArgsConstructor
@NoArgsConstructor
public class Class10 {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String class10_id;
    private String board;
    private String mediumOfStudy;
    private String percentage;
    private String passingYear;

    @OneToOne
    @JoinColumn(name = "education_id", unique = true)
    @JsonIgnore
    private Education education;

    public Class10(Education education) {
        this.education = education;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getClass10_id() {
        return class10_id;
    }

    public void setClass10_id(String class10_id) {
        this.class10_id = class10_id;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public String getMediumOfStudy() {
        return mediumOfStudy;
    }

    public void setMediumOfStudy(String mediumOfStudy) {
        this.mediumOfStudy = mediumOfStudy;
    }

    public String getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(String passingYear) {
        this.passingYear = passingYear;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
