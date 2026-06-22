package com.tnf.training.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;

import com.tnf.training.entity.Feedback;

// Hibernate implementation of FeedbackDAO. HQL assumes Trainer.trainerId and TrainingBatch.batchId (agree with Groups 1 & 2).
public class FeedbackDAOImpl implements FeedbackDAO {

    @Override
    public void save(Session session, Feedback feedback) {
        session.persist(feedback);
    }

    @Override
    public Optional<Feedback> findById(Session session, Long feedbackId) {
        return Optional.ofNullable(session.get(Feedback.class, feedbackId));
    }

    @Override
    public List<Feedback> findAll(Session session) {
        return session.createQuery("FROM Feedback f", Feedback.class).getResultList();
    }

    @Override
    public List<Feedback> findByTrainer(Session session, Long trainerId) {
        return session.createQuery("FROM Feedback f WHERE f.trainer.trainerId = :trainerId", Feedback.class)
                .setParameter("trainerId", trainerId)
                .getResultList();
    }

    @Override
    public List<Feedback> findByBatch(Session session, Long batchId) {
        return session.createQuery("FROM Feedback f WHERE f.trainingBatch.batchId = :batchId", Feedback.class)
                .setParameter("batchId", batchId)
                .getResultList();
    }
}
