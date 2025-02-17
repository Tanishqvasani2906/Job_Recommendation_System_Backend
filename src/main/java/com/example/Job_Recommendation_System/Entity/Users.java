package com.example.Job_Recommendation_System.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
//@Data
public class Users {
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String user_id;
    @Column(name = "username" , nullable = true, unique = true)
    private String username;
    @Column(name = "password" , nullable = true , unique = true)
    private String password;
    @Column(name = "first_name" , nullable = false)
    private String firstName;
    @Column(name = "last_name" , nullable = true)
    private String lastName;
    @Column(name = "email" , nullable = false , unique = true)
    private String email;
    @Column(name = "phone" , nullable = true , unique = true)
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "gender")
    private String gender;
    @Column(name = "resumeUrl" , nullable = true , unique = true)
    private String resumeUrl;

    @Column(name = "temp_token")
    private String tempToken;

    public LocalDateTime getTempTokenExpiry() {
        return tempTokenExpiry;
    }

    public void setTempTokenExpiry(LocalDateTime tempTokenExpiry) {
        this.tempTokenExpiry = tempTokenExpiry;
    }

    public String getTempToken() {
        return tempToken;
    }

    public void setTempToken(String tempToken) {
        this.tempToken = tempToken;
    }

    @Column(name = "temp_token_expiry")
    private LocalDateTime tempTokenExpiry;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles", nullable = false)
    private Role role = Role.EMPLOYEE;

    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<UserJobs> jobs= new ArrayList<>();;

    public List<UserJobs> getJobs() {
        return jobs;
    }

    public void setJobs(List<UserJobs> jobs) {
        this.jobs = jobs;
    }
}
