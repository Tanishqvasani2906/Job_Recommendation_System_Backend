package com.example.Job_Recommendation_System.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResumeUploadDTO {
    private String resumeText;
    private String firebasePdfUrl;
    public String getResumeText() {
        return resumeText;
    }

    public void setResumeText(String resumeText) {
        this.resumeText = resumeText;
    }

    public String getFirebasePdfUrl() {
        return firebasePdfUrl;
    }

    public void setFirebasePdfUrl(String firebasePdfUrl) {
        this.firebasePdfUrl = firebasePdfUrl;
    }
}
