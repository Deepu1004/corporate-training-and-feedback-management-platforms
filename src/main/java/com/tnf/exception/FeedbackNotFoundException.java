package com.tnf.exception;

// Thrown when a feedback lookup by id returns nothing.
public class FeedbackNotFoundException extends RuntimeException {

    public FeedbackNotFoundException(Long feedbackId) {
        super("Feedback not found with id: " + feedbackId);
    }
}
