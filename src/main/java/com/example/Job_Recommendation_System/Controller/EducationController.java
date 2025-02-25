package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.Education;
import com.example.Job_Recommendation_System.Repository.EducationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/education")
public class EducationController {
    @Autowired
    private EducationRepo educationRepo;

    @PostMapping("/addEducation")
    public ResponseEntity<?> createEducation() {
        Education newEducation = new Education();
        Education savedEducation = educationRepo.save(newEducation);
        return ResponseEntity.ok(savedEducation);
    }
    @GetMapping("/{education_id}")
    public ResponseEntity<?> getEducationById(@PathVariable String education_id) {
        Optional<Education> educationOpt = educationRepo.findById(education_id);
        if (educationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Education record not found.");
        }
        return ResponseEntity.ok(educationOpt.get());
    }
}
