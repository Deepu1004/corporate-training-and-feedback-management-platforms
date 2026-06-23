package com.tnf.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.tnf.entity.Trainee;
import com.tnf.util.HibernateUtil;

public class TraineeDAOImpl implements TraineeDAO {

    @Override
    public void registerTrainee(Trainee trainee) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(trainee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public Trainee findTrainee(Long traineeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Trainee.class, traineeId);
        }
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(trainee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteTrainee(Long traineeId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Trainee trainee = session.get(Trainee.class, traineeId);
            if (trainee != null) {
                session.remove(trainee);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Trainee> getAllTrainees() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Trainee", Trainee.class).list();
        }
    }
}
