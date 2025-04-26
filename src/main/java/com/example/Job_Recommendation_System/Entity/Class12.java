package com.example.Job_Recommendation_System.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "class12")
@AllArgsConstructor
//@NoArgsConstructor(force = true)
public class Class12 {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String class12_id;
    @Column(name = "board")
    private String board;
    @Column(name = "medium_of_study")
    private String mediumOfStudy;
    @Column(name = "percentage")
    private String percentage;
    @Column(name = "passing_year")
    private String passingYear;

    @OneToOne
    @JoinColumn(name = "education_id", unique = true)
    @JsonIgnore
    private Education education;

    public Class12(Education education) {
        this.education = education;
    }

    public Class12() {
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getClass12_id() {
        return class12_id;
    }

    public void setClass12_id(String class12_id) {
        this.class12_id = class12_id;
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
