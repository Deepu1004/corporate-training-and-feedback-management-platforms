package com.tnf.exception;

// Thrown when a trainee lookup by id returns nothing.
// Unchecked so callers aren't forced to wrap every search in try/catch.
public class TraineeNotFoundException extends RuntimeException {

    public TraineeNotFoundException(String message) {
        super(message);
    }
}
