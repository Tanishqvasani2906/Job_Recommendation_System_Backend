package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.CareerPreferences;
import com.example.Job_Recommendation_System.Entity.Degree;
import com.example.Job_Recommendation_System.Entity.Education;
import com.example.Job_Recommendation_System.Entity.Users;
import com.example.Job_Recommendation_System.Repository.CareerPreferencesRepo;
import com.example.Job_Recommendation_System.Repository.EducationRepo;
import com.example.Job_Recommendation_System.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/career-preferences")
public class CareerPreferencesController {
    @Autowired
    private UserRepo usersRepo;
    @Autowired
    private CareerPreferencesRepo careerPreferencesRepo;
    @Autowired
    private EducationRepo educationRepo;

    // ✅ 1️⃣ Add Career Preferences for a User
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addCareerPreferences(@PathVariable String userId, @RequestBody CareerPreferences careerPreferences) {
        Optional<Users> userOpt = usersRepo.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User not found."));
        }
        Users user = userOpt.get();
        careerPreferences.setUsers(user);

        // Fetch and attach the Education entity if provided
        if (careerPreferences.getEducation() != null) {
            Optional<Education> eduOpt = educationRepo.findById(careerPreferences.getEducation().getEducation_id());
            if (eduOpt.isPresent()) {
                Education education = eduOpt.get();

                // Ensure relationships are set before saving
                if (education.getDegrees() != null) {
                    for (Degree degree : education.getDegrees()) {
                        degree.setEducation(education);
                    }
                }
                if (education.getClass12() != null) {
                    education.getClass12().setEducation(education);
                }
                if (education.getClass10() != null) {
                    education.getClass10().setEducation(education);
                }
                careerPreferences.setEducation(education);
            }
        }
        CareerPreferences savedPreferences = careerPreferencesRepo.save(careerPreferences);

        // Fetch again to ensure all fields are populated before returning
        return careerPreferencesRepo.findById(savedPreferences.getCareerPreferences_id())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(500).body((CareerPreferences) Collections.singletonMap("error", "Error retrieving saved data")));
    }

    // ✅ 2️⃣ Get Career Preferences by User ID
    @GetMapping("getByUserId/{userId}")
    public ResponseEntity<?> getCareerPreferencesByUser(@PathVariable String userId) {
        Optional<Users> userOpt = usersRepo.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        Optional<CareerPreferences> careerPreferencesOpt = careerPreferencesRepo.findByUsersWithEducation(userOpt.get());
        if (careerPreferencesOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CareerPreferences careerPreferences = careerPreferencesOpt.get();
        System.out.println("Education: " + careerPreferences.getEducation()); // Debugging
        return ResponseEntity.ok(careerPreferences);
    }


    // ✅ 3️⃣ Update Career Preferences
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateCareerPreferences(@PathVariable String userId, @RequestBody CareerPreferences updatedPreferences) {
        Optional<Users> userOpt = usersRepo.findById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        Optional<CareerPreferences> careerPreferencesOpt = careerPreferencesRepo.findByUsers(userOpt.get());

        if (careerPreferencesOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Career Preferences not found for this user.");
        }

        CareerPreferences existingPreferences = careerPreferencesOpt.get();

        // Update only non-null fields
        if (updatedPreferences.getPreferedJobType() != null) existingPreferences.setPreferedJobType(updatedPreferences.getPreferedJobType());
        if (updatedPreferences.getPreferedLocation() != null) existingPreferences.setPreferedLocation(updatedPreferences.getPreferedLocation());
        if (updatedPreferences.getProfileSummary() != null) existingPreferences.setProfileSummary(updatedPreferences.getProfileSummary());
        if (updatedPreferences.getKeySkills() != null) existingPreferences.setKeySkills(updatedPreferences.getKeySkills());
        if (updatedPreferences.getLanguage() != null) existingPreferences.setLanguage(updatedPreferences.getLanguage());

        CareerPreferences savedPreferences = careerPreferencesRepo.save(existingPreferences);
        return ResponseEntity.ok(savedPreferences);
    }

    // ✅ 4️⃣ Delete Career Preferences
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteCareerPreferences(@PathVariable String userId) {
        Optional<Users> userOpt = usersRepo.findById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        Optional<CareerPreferences> careerPreferencesOpt = careerPreferencesRepo.findByUsers(userOpt.get());

        if (careerPreferencesOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Career Preferences not found for this user.");
        }

        careerPreferencesRepo.delete(careerPreferencesOpt.get());
        return ResponseEntity.ok("Career Preferences deleted successfully.");
    }
}
