package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Entity.CareerPreferences;
import com.example.Job_Recommendation_System.Entity.Projects;
import com.example.Job_Recommendation_System.Repository.CareerPreferencesRepo;
import com.example.Job_Recommendation_System.Repository.ProjectsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/project")
public class ProjectsController {

    @Autowired
    private ProjectsRepo projectsRepo;

    @Autowired
    private CareerPreferencesRepo careerPreferencesRepo;

    // ✅ 1️⃣ Add a Project to Career Preferences
    @PostMapping("/add/{careerPreferencesId}")
    public ResponseEntity<?> addProjects(@PathVariable String careerPreferencesId, @RequestBody Projects projects) {
        Optional<CareerPreferences> careerPreferencesOpt = careerPreferencesRepo.findById(careerPreferencesId);

        if (careerPreferencesOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Career preferences not found.");
        }

        CareerPreferences careerPreferences = careerPreferencesOpt.get();
        projects.setCareerPreferences(careerPreferences);  // Associate project with career preferences
        Projects savedProject = projectsRepo.save(projects);

        return ResponseEntity.ok(savedProject);
    }

    // ✅ 2️⃣ Get All Projects by Career Preferences ID
    @GetMapping("/{careerPreferencesId}")
    public ResponseEntity<?> getProjects(@PathVariable String careerPreferencesId) {
        List<Projects> projectsList = projectsRepo.findByCareerPreferencesId(careerPreferencesId);

        if (projectsList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectsList);
    }

    // ✅ 3️⃣ Get a Single Project by ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId) {
        Optional<Projects> projectOpt = projectsRepo.findById(projectId);

        return projectOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ 4️⃣ Update an Existing Project
    @PutMapping("/update/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable String projectId, @RequestBody Projects updatedProject) {
        Optional<Projects> projectOpt = projectsRepo.findById(projectId);

        if (projectOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Project not found.");
        }

        Projects existingProject = projectOpt.get();
        if (updatedProject.getProjectName() != null) existingProject.setProjectName(updatedProject.getProjectName());
        if (updatedProject.getProjectDescription() != null) existingProject.setProjectDescription(updatedProject.getProjectDescription());
        if (updatedProject.getProjectDuration() != null) existingProject.setProjectDuration(updatedProject.getProjectDuration());

        Projects savedProject = projectsRepo.save(existingProject);
        return ResponseEntity.ok(savedProject);
    }

    // ✅ 5️⃣ Delete a Project by ID
    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId) {
        Optional<Projects> projectOpt = projectsRepo.findById(projectId);

        if (projectOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Project not found.");
        }

        projectsRepo.deleteById(projectId);
        return ResponseEntity.ok("Project deleted successfully.");
    }
}
