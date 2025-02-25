package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.CareerPreferences;
import com.example.Job_Recommendation_System.Entity.Internships;
import com.example.Job_Recommendation_System.Repository.CareerPreferencesRepo;
import com.example.Job_Recommendation_System.Repository.InternshipsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/internships")
public class InternshipsController {

    @Autowired
    private InternshipsRepo internshipsRepo;

    @Autowired
    private CareerPreferencesRepo careerPreferencesRepo;

    // ✅ 1️⃣ Add Internship to a Career Preferences Record
    @PostMapping("/add/{careerPreferencesId}")
    public ResponseEntity<?> addInternship(@PathVariable String careerPreferencesId, @RequestBody Internships internship) {
        Optional<CareerPreferences> careerPreferencesOpt = careerPreferencesRepo.findById(careerPreferencesId);

        if (careerPreferencesOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Career Preferences record not found.");
        }

        CareerPreferences careerPreferences = careerPreferencesOpt.get();
        internship.setCareerPreferences(careerPreferences);  // Associate the internship with career preferences

        Internships savedInternship = internshipsRepo.save(internship);
        return ResponseEntity.ok(savedInternship);
    }

    // ✅ 2️⃣ Get All Internships by Career Preferences ID
    @GetMapping("/{careerPreferencesId}")
    public ResponseEntity<?> getInternshipsByCareerPreferences(@PathVariable String careerPreferencesId) {
        List<Internships> internships = internshipsRepo.findByCareerPreferencesId(careerPreferencesId);

        if (internships.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(internships);
    }

    // ✅ 3️⃣ Update Internship Details
    @PutMapping("/update/{internshipId}")
    public ResponseEntity<?> updateInternship(@PathVariable String internshipId, @RequestBody Internships updatedInternship) {
        Optional<Internships> internshipOpt = internshipsRepo.findById(internshipId);

        if (internshipOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Internship record not found.");
        }

        Internships existingInternship = internshipOpt.get();

        // Update fields if provided
        if (updatedInternship.getCompanyName() != null) existingInternship.setCompanyName(updatedInternship.getCompanyName());
        if (updatedInternship.getDurationFrom() != null) existingInternship.setDurationFrom(updatedInternship.getDurationFrom());
        if (updatedInternship.getDurationTo() != null) existingInternship.setDurationTo(updatedInternship.getDurationTo());
        if (updatedInternship.getDescription() != null) existingInternship.setDescription(updatedInternship.getDescription());

        Internships savedInternship = internshipsRepo.save(existingInternship);
        return ResponseEntity.ok(savedInternship);
    }

    // ✅ 4️⃣ Delete Internship
    @DeleteMapping("/delete/{internshipId}")
    public ResponseEntity<?> deleteInternship(@PathVariable String internshipId) {
        Optional<Internships> internshipOpt = internshipsRepo.findById(internshipId);

        if (internshipOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Internship record not found.");
        }

        internshipsRepo.delete(internshipOpt.get());
        return ResponseEntity.ok("Internship deleted successfully.");
    }
}
