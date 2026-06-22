package com.tnf.training.service;

import com.tnf.training.dao.FeedbackDAO;
import com.tnf.training.dao.FeedbackDAOImpl;
import com.tnf.training.entity.Feedback;
import com.tnf.training.entity.Trainee;
import com.tnf.training.entity.Trainer;
import com.tnf.training.entity.TrainingBatch;
import com.tnf.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

// Service layer for Feedback: validation + transaction boundary, delegates persistence to FeedbackDAO.
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackDAO feedbackDAO = new FeedbackDAOImpl();

    @Override
    public Feedback submitFeedback(Long traineeId, Long trainerId, Long batchId, int rating, String comments) {
        validate(traineeId, trainerId, batchId, rating);
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Feedback feedback = new Feedback();
            feedback.setRating(rating);
            feedback.setComments(comments);
            // Link by id only — getReference needs just the type, not the other teams' fields.
            feedback.setTrainee(session.getReference(Trainee.class, traineeId));
            feedback.setTrainer(session.getReference(Trainer.class, trainerId));
            feedback.setTrainingBatch(session.getReference(TrainingBatch.class, batchId));
            feedbackDAO.save(session, feedback);
            tx.commit();
            return feedback;
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback(); // undo partial write on failure
            }
            throw ex;
        }
    }

    @Override
    public Optional<Feedback> getFeedback(Long feedbackId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return feedbackDAO.findById(session, feedbackId);
        }
    }

    @Override
    public List<Feedback> getAllFeedback() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return feedbackDAO.findAll(session);
        }
    }

    @Override
    public List<Feedback> getFeedbackByTrainer(Long trainerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return feedbackDAO.findByTrainer(session, trainerId);
        }
    }

    @Override
    public List<Feedback> getFeedbackByBatch(Long batchId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return feedbackDAO.findByBatch(session, batchId);
        }
    }

    // Reject bad input before opening a transaction.
    private void validate(Long traineeId, Long trainerId, Long batchId, int rating) {
        if (traineeId == null || trainerId == null || batchId == null) {
            throw new IllegalArgumentException("traineeId, trainerId and batchId are required");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("rating must be between 1 and 5");
        }
    }
}
