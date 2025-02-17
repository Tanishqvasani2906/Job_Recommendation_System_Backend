package com.example.Job_Recommendation_System.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobRequestDTO {
    private String title;
    private String companyName;
    private String description;
    private String contactEmail;
    private String tags;
    private String companyUrl;
    private String city;
    private String state;

    public String getCity() {
        return city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public String getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }
}

