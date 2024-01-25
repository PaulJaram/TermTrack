package com.example.termtrack.entities.enums;

public enum TypeAssessment {
    OBJECTIVE,
    PERFORMANCE;

    public static String toString(TypeAssessment type) {
        String typeString = null;
        switch (type) {
            case OBJECTIVE:
                typeString = "Objective Assessment";
                break;
            case PERFORMANCE:
                typeString = "Performance Assessment";
                break;
        }
        return typeString;
    }
}
