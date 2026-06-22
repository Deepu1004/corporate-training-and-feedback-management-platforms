package com.tnf.training.reports;

import java.util.Optional;

// Reporting queries for the Feedback module (Group 4 challenge).
public interface ReportService {

    // Overall average rating across all feedback; null if there is none.
    Double getAverageRating();

    // Trainer with the highest average rating.
    Optional<TrainerRating> getBestTrainer();

    // Trainer with the lowest average rating.
    Optional<TrainerRating> getLowestRatedTrainer();

    // Number of training batches conducted by one trainer.
    long getTrainingsConducted(Long trainerId);

    // Total number of training batches conducted.
    long getTotalTrainingsConducted();
}
