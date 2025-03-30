package com.example.Job_Recommendation_System.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "career_preferences")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareerPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String careerPreferences_id;
    @Column(name = "preffered_job_type")
    private String preferedJobType;
    @Column(name = "preffered_location")
    private String preferedLocation;
    @Column(name = "profile_summary")
    private String profileSummary;
    @Column(name = "key_skills")
    private String keySkills;
    @Column(name = "language")
    private String language;
    @Column(name = "resume_url")  // ✅ New field for storing resume URL
    private String resumeUrl;

    @OneToOne
    @JoinColumn(name = "user_id",unique = true, nullable = false)
    private Users users;

    @OneToMany(mappedBy = "careerPreferences", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER )
    private List<Internships> internships;
    @OneToMany(mappedBy = "careerPreferences",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Projects> projects;

    @OneToOne
    @JoinColumn(name = "education_id", unique = true)
    @JsonManagedReference
    private Education education;


    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public String getCareerPreferences_id() {
        return careerPreferences_id;
    }

    public void setCareerPreferences_id(String careerPreferences_id) {
        this.careerPreferences_id = careerPreferences_id;
    }

    public List<Internships> getInternships() {
        return internships;
    }

    public void setInternships(List<Internships> internships) {
        this.internships = internships;
    }

    public String getKeySkills() {
        return keySkills;
    }

    public void setKeySkills(String keySkills) {
        this.keySkills = keySkills;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPreferedJobType() {
        return preferedJobType;
    }

    public void setPreferedJobType(String preferedJobType) {
        this.preferedJobType = preferedJobType;
    }

    public String getPreferedLocation() {
        return preferedLocation;
    }

    public void setPreferedLocation(String preferedLocation) {
        this.preferedLocation = preferedLocation;
    }

    public String getProfileSummary() {
        return profileSummary;
    }

    public void setProfileSummary(String profileSummary) {
        this.profileSummary = profileSummary;
    }

    public List<Projects> getProjects() {
        return projects;
    }

    public void setProjects(List<Projects> projects) {
        this.projects = projects;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }
}
