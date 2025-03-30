package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.Class10;
import com.example.Job_Recommendation_System.Entity.Class12;
import com.example.Job_Recommendation_System.Entity.Education;
import com.example.Job_Recommendation_System.Repository.Class12Repo;
import com.example.Job_Recommendation_System.Repository.EducationRepo;
import com.example.Job_Recommendation_System.Service.Class12Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/class12")
public class Class12Controller {
    @Autowired
    private Class12Repo class12Repo;
    @Autowired
    private EducationRepo educationRepo;
    @Autowired
    private Class12Service class12Service;

    @PostMapping("/add/{education_id}")
    public ResponseEntity<?> addClass10(@PathVariable String education_id, @RequestBody Class12 class12) {
        Optional<Education> educationOpt = educationRepo.findById(education_id);

        if (educationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Education record not found.");
        }

        class12.setEducation(educationOpt.get()); // Link Class10 to Education
        Class12 savedClass12 = class12Repo.save(class12);
        return ResponseEntity.ok(savedClass12);
    }

    @PutMapping("/update/{class12Id}")
    public ResponseEntity<?> updateClass12(@PathVariable String class12Id, @RequestBody Class12 updatedClass12) {
        Optional<Class12> updated = class12Service.updateClass12(class12Id, updatedClass12);

        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        } else {
            return ResponseEntity.badRequest().body("Class12 record not found.");
        }
    }


}
