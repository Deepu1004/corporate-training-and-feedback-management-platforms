package com.tnf.training.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    private Integer rating;

    private String comments;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private TrainingBatch trainingBatch;

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public Feedback(Long feedbackId, Integer rating, String comments, Trainer trainer, Trainee trainee,
            TrainingBatch trainingBatch) {
        this.feedbackId = feedbackId;
        this.rating = rating;
        this.comments = comments;
        this.trainer = trainer;
        this.trainee = trainee;
        this.trainingBatch = trainingBatch;
    }

    public Feedback() {
    }

    @Override
    public String toString() {
        return "Feedback [feedbackId=" + feedbackId + ", rating=" + rating + ", comments=" + comments + ", trainer="
                + trainer + ", trainee=" + trainee + ", trainingBatch=" + trainingBatch + "]";
    }

    public TrainingBatch getTrainingBatch() {
        return trainingBatch;
    }

    public void setTrainingBatch(TrainingBatch trainingBatch) {
        this.trainingBatch = trainingBatch;
    }

}
