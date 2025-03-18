package com.example.Job_Recommendation_System.Controller;

import com.example.Job_Recommendation_System.Dto.ResumeUploadDTO;
import com.example.Job_Recommendation_System.Service.ATSResumeCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resume/ats")
public class AtsResumeCheckerController{
    @Autowired
    private ATSResumeCheckerService atsResumeCheckerService;

    @PostMapping("/check-general")
    public ResponseEntity<?> getGeneralAtsResumeChecker(@RequestBody ResumeUploadDTO resumeUploadDTO){
        return atsResumeCheckerService.checkGeneralATSScore(resumeUploadDTO);
    }

}
