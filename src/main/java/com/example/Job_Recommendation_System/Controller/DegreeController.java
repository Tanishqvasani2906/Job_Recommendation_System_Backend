package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.Degree;
import com.example.Job_Recommendation_System.Entity.Education;
import com.example.Job_Recommendation_System.Repository.DegreeRepo;
import com.example.Job_Recommendation_System.Repository.EducationRepo;
import com.example.Job_Recommendation_System.Service.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/degree")
public class DegreeController {
    @Autowired
    private EducationRepo educationRepo;
    @Autowired
    private DegreeRepo degreeRepo;
    @Autowired
    private DegreeService degreeService;

    // ✅ 1️⃣ Add Degree to a Specific Education Record
    @PostMapping("/add/{education_id}")
    public ResponseEntity<?> addDegree(@PathVariable String education_id, @RequestBody Degree degree) {
        Optional<Education> educationOpt = educationRepo.findById(education_id);

        if (educationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Education record not found.");
        }

        Education education = educationOpt.get();
        degree.setEducation(education);  // Associate the degree with education

        Degree savedDegree = degreeRepo.save(degree);
        return ResponseEntity.ok(savedDegree);
    }

    // ✅ 2️⃣ Get All Degrees by Education ID
    @GetMapping("/{education_id}")
    public ResponseEntity<?> getDegreesByEducation(@PathVariable String education_id) {
        List<Degree> degrees = degreeRepo.findByEducationId(education_id);


        if (degrees.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(degrees);
    }

    @PutMapping("/update/{degreeId}")
    public ResponseEntity<?> updateDegree(@PathVariable String degreeId, @RequestBody Degree updatedDegree) {
        Optional<Degree> updated = degreeService.updateDegree(degreeId, updatedDegree);

        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        } else {
            return ResponseEntity.badRequest().body("Degree record not found.");
        }
    }
}
