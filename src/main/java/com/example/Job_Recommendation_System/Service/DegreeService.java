package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Entity.Degree;
import com.example.Job_Recommendation_System.Repository.DegreeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DegreeService {
    @Autowired
    private DegreeRepo degreeRepo;

    public Optional<Degree> updateDegree(String degreeId, Degree updatedDegree) {
        return degreeRepo.findById(degreeId).flatMap(existingDegree -> {
            if (updatedDegree.getDegreeName() != null || updatedDegree.getDegreeName() == null) {
                existingDegree.setDegreeName(updatedDegree.getDegreeName());
            }
            if (updatedDegree.getCourseName() != null || updatedDegree.getCourseName() == null) {
                existingDegree.setCourseName(updatedDegree.getCourseName());
            }
            if (updatedDegree.getCGPA() != null || updatedDegree.getCGPA() == null) {
                existingDegree.setCGPA(updatedDegree.getCGPA());
            }
            if (updatedDegree.getCourseDurationFrom() != null || updatedDegree.getCourseDurationFrom() == null) {
                existingDegree.setCourseDurationFrom(updatedDegree.getCourseDurationFrom());
            }
            if (updatedDegree.getCourseDurationTo() != null || updatedDegree.getCourseDurationTo() == null) {
                existingDegree.setCourseDurationTo(updatedDegree.getCourseDurationTo());
            }

            return Optional.of(degreeRepo.save(existingDegree));
        }).or(() -> Optional.empty());
    }

}
