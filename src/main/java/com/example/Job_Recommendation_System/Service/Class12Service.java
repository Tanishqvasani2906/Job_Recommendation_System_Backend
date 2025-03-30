package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Entity.Class12;
import com.example.Job_Recommendation_System.Repository.Class12Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class Class12Service {
    @Autowired
    private Class12Repo class12Repo;

    public Optional<Class12> updateClass12(String class12Id, Class12 updatedClass12) {
        return class12Repo.findById(class12Id).flatMap(existingClass12 -> {
            if (updatedClass12.getBoard() != null || updatedClass12.getBoard() == null) {
                existingClass12.setBoard(updatedClass12.getBoard());
            }
            if (updatedClass12.getMediumOfStudy() != null || updatedClass12.getMediumOfStudy() == null) {
                existingClass12.setMediumOfStudy(updatedClass12.getMediumOfStudy());
            }
            if (updatedClass12.getPercentage() != null || updatedClass12.getPercentage() == null) {
                existingClass12.setPercentage(updatedClass12.getPercentage());
            }
            if (updatedClass12.getPassingYear() != null || updatedClass12.getPassingYear() == null) {
                existingClass12.setPassingYear(updatedClass12.getPassingYear());
            }

            return Optional.of(class12Repo.save(existingClass12)); // No need for another Optional wrapper
        });
    }


}
