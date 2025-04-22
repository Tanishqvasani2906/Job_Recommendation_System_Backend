package com.example.Job_Recommendation_System.Service;

import com.example.Job_Recommendation_System.Dto.ResumeUploadDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@Service
public class ATSResumeCheckerService {

    private static final String API_KEY = "AIzaSyDSMwAXEPn5XRq0imEILpzKGwU6tHhimjU";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public ResponseEntity<?> checkGeneralATSScore(ResumeUploadDTO resumeUploadDTO) {
        String resumeText;

        try {
            // Extract text from Firebase PDF if URL is provided
            if (resumeUploadDTO.getFirebasePdfUrl() != null && !resumeUploadDTO.getFirebasePdfUrl().isEmpty()) {
                resumeText = extractTextFromPdf(resumeUploadDTO.getFirebasePdfUrl());
            }
            // Use provided resume text
            else if (resumeUploadDTO.getResumeText() != null && !resumeUploadDTO.getResumeText().isEmpty()) {
                resumeText = resumeUploadDTO.getResumeText();
            } else {
                return ResponseEntity.badRequest().body("Provide either resume text or Firebase PDF URL.");
            }

            RestTemplate restTemplate = new RestTemplate();

            // Optimized structured prompt for Gemini API
            String prompt = """
Perform a **strict and realistic ATS compatibility analysis** on the given resume based on industry-leading standards, including [Resume Worded](https://resumeworded.com) and [Enhancv](https://enhancv.com). 

### **🔹 Updated Scoring Criteria (STRICTER)**
- **Ensure scoring is dynamic from 1 to 100**, without fixed minimums.  
- **Very poor resumes should score between 10-30**, just like Resume Worded’s strict evaluation.  
- **Use harsher penalties for missing sections, vague descriptions, and ATS-incompatible formatting.**  
- **Do not average scores across categories.** Each section should be **scored independently** based on strict ATS compliance.

---
### **🔹 Strict Resume Validation (Experience Requirement)**
- If the resume **does not contain a section titled "Experience"**, return the following error message:
  ```json
  {
    "error": "This document is not a resume. A resume must include an experience section."
  }


### **🔹 Key Evaluation Areas (Updated for More Precision)**

1️⃣ **Formatting & Structure (Strict Evaluation)**  
   - **Penalize poor formatting more aggressively** (e.g., missing section headers, inconsistent spacing, lack of a clear visual hierarchy).  
   - If the resume lacks an "Objective/Summary" section, **deduct points** because it’s a best practice on Resume Worded.  
   - If inconsistent bullet points are found, **reduce formatting score below 25**.  

2️⃣ **Keyword Optimization & Skills Alignment (New Weighting System)**  
   - **Missing job-specific keywords should heavily reduce the score**.  
   - If a resume does not contain **role-specific skills (e.g., “UI Research”, “A/B Testing”)**, **the score should drop below 20**.  
   - If keywords are only listed in a "Skills" section and not in work experience, **deduct points**.  

3️⃣ **Readability & Clarity (Penalize More for Vague Language)**  
   - **Sentences should be concise, with quantifiable results.**  
   - If a resume lacks **action verbs** and **impact-driven statements**, the **readability score should be below 40**.  
   - If project descriptions are too vague, **drop the content score below 30**.  

4️⃣ **ATS Friendliness & Parseability (Strict ATS Rules)**  
   - **Check if symbols, tables, or multi-column layouts are breaking ATS parsing.** If present, **reduce ATS score drastically**.  
   - If date formats are inconsistent, **deduct 10+ points automatically**.  

5️⃣ **Content Quality & Achievements (More Aggressive Scoring)**  
   - **If no quantifiable achievements are mentioned, drop the score below 30.**  
   - **If the resume is listing responsibilities instead of impact-driven results, deduct points.**  
   - Example:  
     ❌ **"Managed a team of designers."** → BAD  
     ✅ **"Led a team of 5 designers, increasing design efficiency by 30%.** → GOOD  

6️⃣ **Design & Impact (Enhancv Guidelines - More Focus on Branding)**  
   - **Lack of personal branding should significantly impact the score.**  
   - If the resume is **plain and lacks design elements**, deduct points for poor visual structure.  

7️⃣ **Best Practices & External Insights (Reference Resume Worded & Enhancv)**  
   - **If the resume structure does not follow Enhancv's standards (clear sections, strong branding, concise wording), reduce score significantly.**  
   - Compare against **real-world ATS optimization strategies from Resume Worded and other professional resume review tools**.  

---

### **📌 Stricter Response Format (Ensure Proper Scoring)**
```json
{
  "ATS_Score": <dynamic score from 1-100>,
  "Formatting": {
    "Score": <1-100>,
    "Description": "<strict analysis of formatting>",
    "Recommendations": ["<recommendation 1>", "<recommendation 2>"]
  },
  "Keyword_Optimization": {
    "Score": <1-100>,
    "Description": "<analysis of keyword use>",
    "Recommendations": ["<recommendation 1>", "<recommendation 2>"]
  },
  "Readability": {
    "Score": <1-100>,
    "Description": "<sentence clarity and quantifiable results>",
    "Recommendations": ["<recommendation 1>", "<recommendation 2>"]
  },
  "ATS_Friendliness": {
    "Score": <1-100>,
    "Description": "<ATS parsing and formatting issues>",
    "Recommendations": ["<recommendation 1>", "<recommendation 2>"]
  },
  "Content_Quality": {
    "Score": <1-100>,
    "Description": "<impact-driven statements and measurable achievements>",
    "Recommendations": ["<recommendation 1>", "<recommendation 2>"]
  },
  "Design_and_Impact": {
    "Score": <1-100>,
    "Description": "<branding and visual appeal>",
    "Recommendations": ["<recommendation 1>", "<recommendation 2>"]
  },
  "Best_Practices_Insights": {
    "Score": <1-100>,
    "Description": "<comparison with Resume Worded and Enhancv standards>",
    "Recommendations": ["<recommendation 1>", "<recommendation 2>"]
  },
  "Overall_Recommendations": [
    "<final recommendations based on real-world ATS best practices>"
  ]
}


### **Resume to Analyze:**
""" + resumeText;




            // Construct request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", new Object[]{
                    Map.of("role", "user", "parts", new Object[]{Map.of("text", prompt)})
            });

            // Set HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Send request to Gemini API
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, Map.class);

            // Extract and format the response
            return formatResponse(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing resume: " + e.getMessage());
        }
    }


    private ResponseEntity<?> formatResponse(Map<String, Object> responseBody) {
        try {
            if (!responseBody.containsKey("candidates")) {
                return ResponseEntity.badRequest().body("Invalid response format from Gemini API.");
            }

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            if (candidates.isEmpty()) {
                return ResponseEntity.badRequest().body("No valid response from Gemini API.");
            }

            Map<String, Object> firstCandidate = candidates.get(0);
            if (!firstCandidate.containsKey("content")) {
                return ResponseEntity.badRequest().body("Missing 'content' in response.");
            }

            Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");

            if (!content.containsKey("parts")) {
                return ResponseEntity.badRequest().body("Missing 'parts' in response.");
            }

            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            if (parts.isEmpty() || !parts.get(0).containsKey("text")) {
                return ResponseEntity.badRequest().body("No text response received.");
            }

            // Get the raw text response from Gemini
            String rawResponseText = (String) parts.get(0).get("text");

            // Try to extract structured JSON from text
            Map<String, Object> structuredResponse = extractJsonFromText(rawResponseText);

            return ResponseEntity.ok(structuredResponse);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error formatting response: " + e.getMessage());
        }
    }

    private Map<String, Object> extractJsonFromText(String text) {
        try {
            // Find the first '{' and last '}' to extract JSON content
            int startIndex = text.indexOf("{");
            int endIndex = text.lastIndexOf("}");

            if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
                throw new IllegalArgumentException("No valid JSON found in the response.");
            }

            // Extract only the JSON part from the text
            String jsonContent = text.substring(startIndex, endIndex + 1);

            // Parse JSON using Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonContent, Map.class);
        } catch (Exception e) {
            return Map.of("error", "Failed to parse JSON response: " + e.getMessage());
        }
    }


    private Map<String, Object> parseResponseText(String text) {
        Map<String, Object> parsedResponse = new HashMap<>();

        // Extract ATS Score properly
        parsedResponse.put("ATS_Score", extractScore(text));

        // Extract different sections
        parsedResponse.put("Formatting", extractSection(text, "Formatting"));
        parsedResponse.put("Keyword_Optimization", extractSection(text, "Keyword Optimization"));
        parsedResponse.put("Readability", extractSection(text, "Readability"));
        parsedResponse.put("ATS_Friendliness", extractSection(text, "ATS Friendliness"));
        parsedResponse.put("Content_Quality", extractSection(text, "Content Quality"));

        // Extract recommendations
        parsedResponse.put("Recommendations", extractRecommendations(text));

        return parsedResponse;
    }


    private int extractScore(String text) {
        try {
            Pattern pattern = Pattern.compile("ATS Score:\\s*(\\d+)/100");
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));  // Get the matched score
            }
        } catch (Exception e) {
            return 0;
        }
        return 0; // Return 0 if score is not found
    }


    private String extractSection(String text, String sectionTitle) {
        try {
            int start = text.indexOf(sectionTitle);
            if (start == -1) return "N/A";  // Section not found

            int end = text.indexOf("\n\n", start + sectionTitle.length());
            if (end == -1) end = text.length(); // If no next section, take till end

            String extractedText = text.substring(start + sectionTitle.length(), end).trim();

            // Remove unwanted characters
            extractedText = extractedText.replace(":", "").replace("\n", "").trim();

            return extractedText;
        } catch (Exception e) {
            return "N/A";
        }
    }


    private List<String> extractRecommendations(String text) {
        try {
            List<String> recommendations = new ArrayList<>();
            int start = text.indexOf("Recommendations");
            if (start == -1) return List.of("No recommendations available.");

            String[] lines = text.substring(start).split("\n");
            for (String line : lines) {
                if (line.startsWith("*") || line.startsWith("-")) { // Check bullet points
                    recommendations.add(line.replace("*", "").replace("-", "").trim());
                }
            }
            return recommendations;
        } catch (Exception e) {
            return List.of("No recommendations available.");
        }
    }





    // Function to download PDF and extract text
    private String extractTextFromPdf(String pdfUrl) throws IOException {
        URL url = new URL(pdfUrl);
        try (PDDocument document = PDDocument.load(url.openStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}
