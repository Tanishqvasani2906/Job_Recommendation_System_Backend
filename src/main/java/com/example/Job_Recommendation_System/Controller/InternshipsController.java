package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.CareerPreferences;
import com.example.Job_Recommendation_System.Entity.Internships;
import com.example.Job_Recommendation_System.Repository.CareerPreferencesRepo;
import com.example.Job_Recommendation_System.Repository.InternshipsRepo;
import com.example.Job_Recommendation_System.Service.InternshipsService;
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

    @Autowired
    private InternshipsService internshipsService;

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

    // ✅ Update Internship Details
    @PutMapping("/update/{internshipId}")
    public ResponseEntity<?> updateInternship(@PathVariable String internshipId, @RequestBody Internships updatedInternship) {
        try {
            Optional<Internships> updated = internshipsService.updateInternship(internshipId, updatedInternship);

            if (updated.isEmpty()) {
                return ResponseEntity.badRequest().body("Internship record not found.");  // ✅ Keep this as a String response
            }

            return ResponseEntity.ok(updated.get()); // ✅ Successfully return Internship object
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // ✅ Error as String
        }
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
