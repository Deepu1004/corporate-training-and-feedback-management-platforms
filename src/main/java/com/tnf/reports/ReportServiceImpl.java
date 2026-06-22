package com.tnf.training.reports;

import com.tnf.training.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

// Read-only HQL aggregates. Trainer/TrainingBatch are referenced by name/id only (agree names with Groups 1 & 2).
public class ReportServiceImpl implements ReportService {

    @Override
    public Double getAverageRating() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT AVG(f.rating) FROM Feedback f", Double.class).uniqueResult();
        }
    }

    @Override
    public Optional<TrainerRating> getBestTrainer() {
        return topTrainerByRating(true);
    }

    @Override
    public Optional<TrainerRating> getLowestRatedTrainer() {
        return topTrainerByRating(false);
    }

    @Override
    public long getTrainingsConducted(Long trainerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT COUNT(b) FROM TrainingBatch b WHERE b.trainer.trainerId = :trainerId", Long.class)
                    .setParameter("trainerId", trainerId)
                    .uniqueResult();
        }
    }

    @Override
    public long getTotalTrainingsConducted() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(b) FROM TrainingBatch b", Long.class).uniqueResult();
        }
    }

    // Top trainer by average rating: descending = best, ascending = lowest.
    private Optional<TrainerRating> topTrainerByRating(boolean descending) {
        String direction = descending ? "DESC" : "ASC";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> rows = session.createQuery(
                            "SELECT f.trainer.trainerId, f.trainer.trainerName, AVG(f.rating) FROM Feedback f "
                                    + "GROUP BY f.trainer.trainerId, f.trainer.trainerName "
                                    + "ORDER BY AVG(f.rating) " + direction, Object[].class)
                    .setMaxResults(1)
                    .getResultList();
            if (rows.isEmpty()) {
                return Optional.empty();
            }
            Object[] row = rows.get(0);
            return Optional.of(new TrainerRating((Long) row[0], (String) row[1], (Double) row[2]));
        }
    }
}
