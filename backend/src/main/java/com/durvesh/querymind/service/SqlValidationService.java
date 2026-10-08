package com.durvesh.querymind.service;

import org.springframework.stereotype.Service;

@Service
public class SqlValidationService {

    private static final String[] BLOCKED_KEYWORDS = {
        "DROP", "DELETE", "UPDATE", "INSERT", "ALTER", "TRUNCATE"
    };

    public boolean isValid(String sql) {
        String cleaned = sql.trim().toUpperCase();

        if (!cleaned.startsWith("SELECT")) {
            return false;
        }
        if (cleaned.contains(";") && cleaned.indexOf(";") < cleaned.length() - 1) {
            return false;
        }
        for (String keyword : BLOCKED_KEYWORDS) {
            if (cleaned.contains(keyword)) {
                return false;
            }
        }
        return true;
    }

    public String reasonInvalid(String sql) {
        String cleaned = sql.trim().toUpperCase();

        if (!cleaned.startsWith("SELECT")) {
            return "Only SELECT queries are allowed.";
        }
        if (cleaned.contains(";") && cleaned.indexOf(";") < cleaned.length() - 1) {
            return "Multiple statements are not allowed.";
        }
        for (String keyword : BLOCKED_KEYWORDS) {
            if (cleaned.contains(keyword)) {
                return "Query contains a blocked keyword: " + keyword;
            }
        }
        return "Unknown validation error.";
    }
}
