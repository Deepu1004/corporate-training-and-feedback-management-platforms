package com.tnf.training.reports;

// A trainer's average rating, used by the best/lowest trainer reports.
public final class TrainerRating {

    private final Long trainerId;
    private final String trainerName;
    private final Double averageRating;

    public TrainerRating(Long trainerId, String trainerName, Double averageRating) {
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.averageRating = averageRating;
    }

    public Long trainerId() {
        return trainerId;
    }

    public String trainerName() {
        return trainerName;
    }

    public Double averageRating() {
        return averageRating;
    }

}