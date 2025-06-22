package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Dto.JobApplicationQuestionsDTO;
import com.example.Job_Recommendation_System.Dto.JobApplicationRequestDTO;
import com.example.Job_Recommendation_System.Dto.JobApplicationResponseDTO;
import com.example.Job_Recommendation_System.Entity.JobApplication;
import com.example.Job_Recommendation_System.Entity.UserJobs;
import com.example.Job_Recommendation_System.Entity.Users;
import com.example.Job_Recommendation_System.Repository.JobApplicationRepo;
import com.example.Job_Recommendation_System.Repository.UserJobsRepo;
import com.example.Job_Recommendation_System.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepo jobApplicationRepo;
    @Autowired
    private UserJobsRepo userJobsRepo;
    @Autowired
    private UserRepo userRepository;

    @Transactional
    public JobApplicationResponseDTO applyToJob(JobApplicationRequestDTO dto) {
        // Find the job from userJobsRepo
        UserJobs job = userJobsRepo.findById(dto.getUserJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Find the user from userRepository
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Construct the JobApplication entity
        JobApplication application = new JobApplication();
//        application.setApplicationId(UUID.randomUUID().toString()); // generate unique ID
        application.setUser(user);
        application.setJob(job);
        application.setAppliedAt(java.time.LocalDateTime.now());
        application.setStatus("PENDING"); // default status

        // Now set custom answers
        if (dto.getAnswers() != null) {
            var answers = dto.getAnswers();

            application.setTechStack(answers.getTechStack());
            application.setGithub(answers.getGithub());
            application.setProjects(answers.getProjects());
            application.setExperienceYears(answers.getExperienceYears());
            application.setCertifications(answers.getCertifications());
            application.setRoleInterest(answers.getRoleInterest());
            application.setProblemSolving(answers.getProblemSolving());
            application.setTeamworkExperience(answers.getTeamworkExperience());
            application.setRemoteSetup(answers.getRemoteSetup());
            application.setSalaryExpectation(answers.getSalaryExpectation());
            application.setLearningGoal(answers.getLearningGoal());
            application.setCareerGoals(answers.getCareerGoals());
            application.setOpenSource(answers.getOpenSource());

            // Optional: If you want to save all custom answers in one JSON string (customAnswers field)
            StringBuilder customAnswersBuilder = new StringBuilder();
            customAnswersBuilder.append("Tech Stack: ").append(answers.getTechStack()).append("\n")
                    .append("Github Link: ").append(answers.getGithub()).append("\n")
                    .append("Projects: ").append(answers.getProjects()).append("\n")
                    .append("Years of Experience: ").append(answers.getExperienceYears()).append("\n")
                    .append("Certifications: ").append(answers.getCertifications()).append("\n")
                    .append("Role Interest: ").append(answers.getRoleInterest()).append("\n")
                    .append("Problem Solving: ").append(answers.getProblemSolving()).append("\n")
                    .append("Teamwork Experience: ").append(answers.getTeamworkExperience()).append("\n")
                    .append("Remote Setup: ").append(answers.getRemoteSetup()).append("\n")
                    .append("Salary Expectation: ").append(answers.getSalaryExpectation()).append("\n")
                    .append("Learning Goal: ").append(answers.getLearningGoal()).append("\n")
                    .append("Career Goals: ").append(answers.getCareerGoals()).append("\n")
                    .append("Open Source Contribution: ").append(answers.getOpenSource());
            application.setCustomAnswers(customAnswersBuilder.toString());
        }

        // Save to the database
        JobApplication savedApplication = jobApplicationRepo.save(application);

        // Prepare and return the response DTO
        JobApplicationResponseDTO responseDTO = new JobApplicationResponseDTO();
        responseDTO.setApplicationId(savedApplication.getApplicationId());
        responseDTO.setJobTitle(job.getTitle());
        responseDTO.setCompanyName(job.getCompanyName());
        responseDTO.setApplicantName(user.getFirstName());
        responseDTO.setAppliedAt(savedApplication.getAppliedAt());
        responseDTO.setStatus(savedApplication.getStatus());

        responseDTO.setResponses(dto.getAnswers());

        return responseDTO;
    }

    public List<JobApplicationResponseDTO> getApplicationsByUser(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<JobApplication> applications = jobApplicationRepo.findByUser(user);

        return applications.stream().map(app -> {
            JobApplicationResponseDTO dto = new JobApplicationResponseDTO();
            dto.setApplicationId(app.getApplicationId());
            dto.setJobTitle(app.getJob().getTitle());
            dto.setCompanyName(app.getJob().getCompanyName());
            dto.setStatus(app.getStatus()); // ✅ get actual status, not hardcoded
            dto.setApplicantName(user.getUsername());
            dto.setAppliedAt(app.getAppliedAt());
            // Optional: set responses (answers) if you want here
            dto.setResponses(buildAnswersFromApplication(app));
            return dto;
        }).collect(Collectors.toList());
    }

    public List<JobApplicationResponseDTO> getApplicationsForJob(String jobId) {
        UserJobs job = userJobsRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        List<JobApplication> applications = jobApplicationRepo.findByJob(job);

        return applications.stream().map(app -> {
            JobApplicationResponseDTO dto = new JobApplicationResponseDTO();
            dto.setApplicationId(app.getApplicationId());
            dto.setJobTitle(job.getTitle());
            dto.setCompanyName(job.getCompanyName());
            dto.setStatus(app.getStatus()); // ✅ get actual status
            dto.setApplicantName(app.getUser().getUsername());
            dto.setAppliedAt(app.getAppliedAt());
            dto.setResponses(buildAnswersFromApplication(app));
            return dto;
        }).collect(Collectors.toList());
    }

    public JobApplicationResponseDTO getApplicationById(String applicationId) {
        JobApplication app = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        JobApplicationResponseDTO dto = new JobApplicationResponseDTO();
        dto.setApplicationId(app.getApplicationId());
        dto.setJobTitle(app.getJob().getTitle());
        dto.setCompanyName(app.getJob().getCompanyName());
        dto.setStatus(app.getStatus()); // ✅ get actual status
        dto.setApplicantName(app.getUser().getUsername());
        dto.setAppliedAt(app.getAppliedAt());
        dto.setResponses(buildAnswersFromApplication(app)); // ✅ very important
        return dto;
    }

    private JobApplicationQuestionsDTO buildAnswersFromApplication(JobApplication app) {
        JobApplicationQuestionsDTO answers = new JobApplicationQuestionsDTO();
        answers.setTechStack(app.getTechStack());
        answers.setGithub(app.getGithub());
        answers.setProjects(app.getProjects());
        answers.setExperienceYears(app.getExperienceYears());
        answers.setCertifications(app.getCertifications());
        answers.setRoleInterest(app.getRoleInterest());
        answers.setProblemSolving(app.getProblemSolving());
        answers.setTeamworkExperience(app.getTeamworkExperience());
        answers.setRemoteSetup(app.getRemoteSetup());
        answers.setSalaryExpectation(app.getSalaryExpectation());
        answers.setLearningGoal(app.getLearningGoal());
        answers.setCareerGoals(app.getCareerGoals());
        answers.setOpenSource(app.getOpenSource());
        return answers;
    }



    @Transactional
    public JobApplicationResponseDTO updateApplicationStatus(String applicationId, String newStatus) {
        // Validate status
        List<String> validStatuses = List.of("PENDING", "REJECTED", "CONTACTED");
        if (!validStatuses.contains(newStatus.toUpperCase())) {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }

        // Find the application
        JobApplication application = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Update status
        application.setStatus(newStatus.toUpperCase());

        // Save updated application
        JobApplication updatedApplication = jobApplicationRepo.save(application);

        JobApplicationResponseDTO responseDTO = new JobApplicationResponseDTO();
        responseDTO.setApplicationId(updatedApplication.getApplicationId());
        responseDTO.setStatus(updatedApplication.getStatus());
        responseDTO.setAppliedAt(updatedApplication.getAppliedAt());
        responseDTO.setJobTitle(updatedApplication.getJob().getTitle()); // Assuming Job entity has title
        responseDTO.setCompanyName(updatedApplication.getJob().getCompanyName()); // Assuming Job entity has companyName
        responseDTO.setApplicantName(updatedApplication.getUser().getFirstName()); // Assuming User entity has firstName

        return responseDTO;
    }
}
