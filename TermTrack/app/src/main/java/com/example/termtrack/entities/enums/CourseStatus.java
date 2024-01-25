package com.example.termtrack.entities.enums;

public enum CourseStatus {
    IN_PROGRESS,
    COMPLETED,
    DROPPED,
    PLAN_TO_TAKE;

    public static String toString(CourseStatus status) {
        String statusString = null;
        switch (status) {
            case IN_PROGRESS:
                statusString = "In Progress";
                break;
            case COMPLETED:
                statusString = "Completed";
                break;
            case DROPPED:
                statusString = "Dropped";
                break;
            case PLAN_TO_TAKE:
                statusString = "Plan To Take";
                break;
        }
        return statusString;
    }
}
