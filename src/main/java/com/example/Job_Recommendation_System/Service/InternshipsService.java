package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Entity.Internships;
import com.example.Job_Recommendation_System.Repository.InternshipsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InternshipsService {
    @Autowired
    private InternshipsRepo internshipsRepo;

    public Optional<Internships> updateInternship(String internshipId, Internships updatedInternship) {
        return internshipsRepo.findById(internshipId).flatMap(existingInternship -> {
            if (updatedInternship.getCompanyName() != null || updatedInternship.getCompanyName() == null) {
                existingInternship.setCompanyName(updatedInternship.getCompanyName());
            }
            if (updatedInternship.getDurationFrom() != null || updatedInternship.getDurationFrom() == null) {
                existingInternship.setDurationFrom(updatedInternship.getDurationFrom());
            }
            if (updatedInternship.getDurationTo() != null || updatedInternship.getDurationTo() == null) {
                existingInternship.setDurationTo(updatedInternship.getDurationTo());
            }
            if (updatedInternship.getDescription() != null || updatedInternship.getDescription() == null) {
                existingInternship.setDescription(updatedInternship.getDescription());
            }

            // Ensure start date is before end date
            if (existingInternship.getDurationFrom() != null && existingInternship.getDurationTo() != null &&
                    existingInternship.getDurationFrom().isAfter(existingInternship.getDurationTo())) {
                throw new IllegalArgumentException("Start date must be before end date");
            }

            return Optional.of(internshipsRepo.save(existingInternship));
        }).or(() -> Optional.empty());
    }

}
