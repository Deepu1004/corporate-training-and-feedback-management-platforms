package com.tnf.service;

import com.tnf.entity.Feedback;

import java.util.List;

// Business operations for the Feedback module (Group 4).
public interface FeedbackService {

    // Validate and store a feedback; returns the saved feedback.
    // Throws InvalidFeedbackException if the input or referenced ids are invalid.
    Feedback submitFeedback(Long traineeId, Long trainerId, Long batchId, int rating, String comments);

    // View one feedback by id; throws FeedbackNotFoundException if it does not exist.
    Feedback getFeedback(Long feedbackId);

    // View all feedback.
    List<Feedback> getAllFeedback();

    // Trainer-wise feedback.
    List<Feedback> getFeedbackByTrainer(Long trainerId);

    // Batch-wise feedback.
    List<Feedback> getFeedbackByBatch(Long batchId);
}
