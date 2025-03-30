package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.Class10;
import com.example.Job_Recommendation_System.Entity.Education;
import com.example.Job_Recommendation_System.Repository.Class10Repo;
import com.example.Job_Recommendation_System.Repository.EducationRepo;
import com.example.Job_Recommendation_System.Service.Class10Service;
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
    @Autowired
    private Class10Service class10Service;

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

    @PutMapping("/update/{class10Id}")
    public ResponseEntity<?> updateClass10(@PathVariable String class10Id, @RequestBody Class10 updatedClass10) {
        try {
            Optional<Class10> savedClass10 = class10Service.updateClass10(class10Id, updatedClass10);
            return ResponseEntity.ok(savedClass10);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
