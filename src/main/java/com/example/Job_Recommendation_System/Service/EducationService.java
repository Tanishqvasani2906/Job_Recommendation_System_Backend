package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Entity.Education;
import com.example.Job_Recommendation_System.Repository.EducationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationService {
    @Autowired
    private EducationRepo educationRepo;

    public Optional<Education> updateEducation(String educationId, Education updatedEducation) {
        return educationRepo.findById(educationId).flatMap(existingEducation -> {
            if (updatedEducation.getDegrees() != null || updatedEducation.getDegrees() == null) {
                existingEducation.setDegrees(updatedEducation.getDegrees());
            }
            if (updatedEducation.getClass10() != null || updatedEducation.getClass10() == null) {
                existingEducation.setClass10(updatedEducation.getClass10());
            }
            if (updatedEducation.getClass12() != null || updatedEducation.getClass12() == null) {
                existingEducation.setClass12(updatedEducation.getClass12());
            }
            if (updatedEducation.getCareerPreferences() != null || updatedEducation.getCareerPreferences() == null) {
                existingEducation.setCareerPreferences(updatedEducation.getCareerPreferences());
            }

            return Optional.of(educationRepo.save(existingEducation));
        }).or(() -> Optional.empty());
    }

}
