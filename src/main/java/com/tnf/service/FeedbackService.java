package com.tnf.training.service;

import com.tnf.training.entity.Feedback;

import java.util.List;
import java.util.Optional;

// Business operations for the Feedback module (Group 4).
public interface FeedbackService {

    // Validate and store a feedback; returns the saved feedback.
    Feedback submitFeedback(Long traineeId, Long trainerId, Long batchId, int rating, String comments);

    // View one feedback by id.
    Optional<Feedback> getFeedback(Long feedbackId);

    // View all feedback.
    List<Feedback> getAllFeedback();

    // Trainer-wise feedback.
    List<Feedback> getFeedbackByTrainer(Long trainerId);

    // Batch-wise feedback.
    List<Feedback> getFeedbackByBatch(Long batchId);
}
