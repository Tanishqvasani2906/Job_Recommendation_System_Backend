package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.Class10;
import com.example.Job_Recommendation_System.Entity.Education;
import com.example.Job_Recommendation_System.Repository.Class10Repo;
import com.example.Job_Recommendation_System.Repository.EducationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/class10")
public class Class10Controller {
    @Autowired
    private EducationRepo educationRepo;
    @Autowired
    private Class10Repo class10Repo;

    // ✅ Add Class10 record (Requires education_id)
    @PostMapping("/add/{education_id}")
    public ResponseEntity<?> addClass10(@PathVariable String education_id, @RequestBody Class10 class10) {
        Optional<Education> educationOpt = educationRepo.findById(education_id);

        if (educationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Education record not found.");
        }

        class10.setEducation(educationOpt.get()); // Link Class10 to Education
        Class10 savedClass10 = class10Repo.save(class10);
        return ResponseEntity.ok(savedClass10);
    }

    // ✅ Fetch Class10 by ID
    @GetMapping("/{class10_id}")
    public ResponseEntity<?> getClass10ById(@PathVariable String class10_id) {
        Optional<Class10> class10Opt = class10Repo.findById(class10_id);

        if (class10Opt.isEmpty()) {
            return ResponseEntity.badRequest().body("Class 10 record not found.");
        }

        return ResponseEntity.ok(class10Opt.get());
    }
}
