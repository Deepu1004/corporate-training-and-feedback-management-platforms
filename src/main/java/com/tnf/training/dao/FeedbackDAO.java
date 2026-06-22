package com.tnf.training.dao;

import com.tnf.training.entity.Feedback;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

// Persistence contract for Feedback (Group 4). The service passes the Session so it owns the transaction.
public interface FeedbackDAO {

    // Persist a new feedback in the given session.
    void save(Session session, Feedback feedback);

    // Find one feedback by id, or empty if not found.
    Optional<Feedback> findById(Session session, Long feedbackId);

    // Get all feedback records.
    List<Feedback> findAll(Session session);

    // Get all feedback for a trainer.
    List<Feedback> findByTrainer(Session session, Long trainerId);

    // Get all feedback for a batch.
    List<Feedback> findByBatch(Session session, Long batchId);
}
