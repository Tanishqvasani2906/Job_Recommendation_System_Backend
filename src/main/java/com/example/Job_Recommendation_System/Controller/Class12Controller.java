package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.Class10;
import com.example.Job_Recommendation_System.Entity.Class12;
import com.example.Job_Recommendation_System.Entity.Education;
import com.example.Job_Recommendation_System.Repository.Class12Repo;
import com.example.Job_Recommendation_System.Repository.EducationRepo;
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

}
