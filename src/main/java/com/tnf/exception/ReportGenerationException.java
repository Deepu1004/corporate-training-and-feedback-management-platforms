package com.tnf.exception;

// Thrown by the reports/review layer when a report query fails to execute.
// Wraps the underlying cause so the original Hibernate/SQL error isn't lost.
public class ReportGenerationException extends RuntimeException {

    public ReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
