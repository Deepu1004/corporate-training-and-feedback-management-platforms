package com.tnf.service;

import com.tnf.dao.TraineeDAO;
import com.tnf.dao.TraineeDAOImpl;
import com.tnf.entities.Trainee;
import com.tnf.exception.TraineeNotFoundException;

import java.util.List;
import java.util.Optional;

public class TraineeServiceImpl implements TraineeService {

    private TraineeDAO traineeDAO = new TraineeDAOImpl();

    // Unchanged: registration just delegates to the DAO.
    @Override
    public void registerTrainee(Trainee trainee) {
        traineeDAO.registerTrainee(trainee);
    }

    // The DAO can hand back null, so wrap it in an Optional and let the
    // caller decide what to do with an absent trainee.
    @Override
    public Optional<Trainee> searchTrainee(Long traineeId) {
        return Optional.ofNullable(traineeDAO.findTrainee(traineeId));
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        Long traineeId = trainee.getTraineeId();
        Optional.ofNullable(traineeDAO.findTrainee(traineeId))
                .orElseThrow(() -> new TraineeNotFoundException(
                        "Trainee not found with id: " + traineeId));

        traineeDAO.updateTrainee(trainee);
    }

    @Override
    public void deleteTrainee(Long traineeId) {
        Trainee trainee = Optional.ofNullable(traineeDAO.findTrainee(traineeId))
                .orElseThrow(() -> new TraineeNotFoundException(
                        "Trainee not found with id: " + traineeId));

        traineeDAO.deleteTrainee(trainee.getTraineeId());
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeDAO.getAllTrainees();
    }
}
