package com.example.demo.enums;

public enum QuestionType {
    BASIC(1, "Basic"),
    COMPREHENSION(2, "Comprehension"),
    INTEGER(3, "Integer"),
    MATRIX(4, "Matrix"),
    ASSERTION_AND_REASON(5, "Assertion & Reason"),
    NUMERIC(6, "Numeric"),
    COLUMN_MATCHING(7, "Column Matching");

    private final int id;
    private final String displayName;

    // Constructor
    QuestionType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Getter for display name
    public String getDisplayName() {
        return displayName;
    }

    // Method to get QuestionType by ID
    public static QuestionType getById(int id) {
        for (QuestionType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("No QuestionType found for id: " + id);
    }

    // Method to get QuestionType by display name
    public static QuestionType getByDisplayName(String displayName) {
        for (QuestionType type : values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No QuestionType found for display name: " + displayName);
    }
}

