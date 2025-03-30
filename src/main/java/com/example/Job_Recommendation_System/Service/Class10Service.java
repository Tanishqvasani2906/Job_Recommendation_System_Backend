package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Entity.Class10;
import com.example.Job_Recommendation_System.Repository.Class10Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Class10Service {

    @Autowired
    private Class10Repo class10Repository;

    public Optional<Class10> getClass10ById(String class10Id) {
        return class10Repository.findById(class10Id);
    }

    public Optional<Class10> updateClass10(String class10Id, Class10 updatedClass10) {
        return class10Repository.findById(class10Id).map(existingClass10 -> {
            // Allow null values in updates
            existingClass10.setBoard(updatedClass10.getBoard());
            existingClass10.setMediumOfStudy(updatedClass10.getMediumOfStudy());
            existingClass10.setPercentage(updatedClass10.getPercentage());
            existingClass10.setPassingYear(updatedClass10.getPassingYear());

            return class10Repository.save(existingClass10);
        });
    }


}
