package com.example.Job_Recommendation_System.Config;

import com.example.Job_Recommendation_System.Entity.Role;

public class RoleUtils {

    /**
     * Normalize the role string by removing any "ROLE_" prefix
     * and mapping it to the Role enum.
     *
     * @param roleString the role string extracted from the JWT token
     * @return the corresponding Role enum
     * @throws IllegalArgumentException if the role is invalid or cannot be mapped
     */
    public static Role normalizeRole(String roleString) {
        if (roleString == null || roleString.isEmpty()) {
            throw new IllegalArgumentException("Role string is null or empty");
        }

        // Remove the "ROLE_" prefix if it exists
        String normalizedRole = roleString.startsWith("ROLE_")
                ? roleString.substring(5)
                : roleString;

        // Attempt to match the normalized role with the Role enum
        try {
            return Role.valueOf(normalizedRole);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + roleString);
        }
    }
}
