package com.tnf.exception;

// Thrown when feedback input is invalid: missing/unknown trainee, trainer or batch,
// or a rating outside the allowed 1-5 range.
// Unchecked so callers aren't forced to wrap every submit in try/catch.
public class InvalidFeedbackException extends RuntimeException {

    public InvalidFeedbackException(String message) {
        super(message);
    }
}
