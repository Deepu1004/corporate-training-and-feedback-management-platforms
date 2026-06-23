package com.tnf.service;
 
import com.tnf.dao.TrainerDAO;
import com.tnf.entity.Trainer;

import java.util.Collections;
import java.util.List;
 
public class TrainerService {
 
    private final TrainerDAO trainerDAO = new TrainerDAO();
 
    // Register Trainer
    public Trainer registerTrainer(Trainer trainer) {
 
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer object cannot be null");
        }
 
        if (trainer.getName() == null || trainer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Trainer name cannot be empty");
        }
 
        if (trainer.getExperience() < 0) {
            throw new IllegalArgumentException("Experience cannot be negative");
        }
 
        trainerDAO.addTrainer(trainer);
        return trainer;
    }
 
    // Update Trainer
    public void updateTrainer(Trainer trainer) {
 
        if (trainer == null || trainer.getId() == null) {
            throw new IllegalArgumentException("Trainer or Trainer ID cannot be null");
        }
 
        trainerDAO.updateTrainer(trainer);
    }
 
    // Delete Trainer
    public void deleteTrainer(Long id) {
 
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid Trainer ID");
        }
 
        trainerDAO.deleteTrainer(id);
    }

    // Search Trainer by ID
    public Trainer searchTrainer(Long id){
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid Trainer ID");
        }
        return trainerDAO.getTrainerById(id).orElseThrow(() -> new RuntimeException("Trainer not found"));
        
    }

    // Display All Trainers
    public List<Trainer> displayAll(){
        return trainerDAO.showAllTrainer().orElse(Collections.emptyList()); 
    }
}