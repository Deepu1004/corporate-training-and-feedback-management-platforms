package com.tnf.dao;

import com.entities.Trainer;
import com.util.HibernateUtil;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;

public class TrainerDAO {

    Transaction t = null;

    // Add Trainer
    public void addTrainer(Trainer trainer){
        Transaction t = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            t = session.beginTransaction();
            session.persist(trainer);
            t.commit();
        } catch (Exception e){
            if (t != null){
                t.rollback();
            }
            e.printStackTrace();
        }
    }

    // Search Trainer - by ID 
    public Optional<Trainer> getTrainerById(Long id){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return Optional.ofNullable(session.get(Trainer.class, id));
        }
    }

    // Update Trainer
    public void updateTrainer(Trainer trainer){
        Transaction t = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            t = session.beginTransaction();
            session.merge(trainer);
            t.commit();
        } catch (Exception e){
            if (t != null){
                t.rollback();
            }
            e.printStackTrace();
        }
    }

    // Delete Trainer
    public void deleteTrainer(Long id){
        Transaction txn = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            txn = session.beginTransaction();
            Trainer t = session.get(Trainer.class, id);
            if (t != null){
                session.remove(t);
            }
            txn.commit();
        } catch (Exception e){
            if (txn != null){
                txn.rollback();
            }
            e.printStackTrace();
        }
    }

    // View all Trainers
    public Optional<List<Trainer>> showAllTrainer(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            List<Trainer> trainers = session.createQuery("FROM Trainer", Trainer.class).getResultList();
            return Optional.of(trainers); // never ofNullable, list won't be null
        } catch (Exception e) {
        e.printStackTrace();
        return Optional.empty(); // signals something went wrong
    }
    }
}