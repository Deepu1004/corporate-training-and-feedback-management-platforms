package com.tnf.service;

import com.tnf.entities.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeService {

    void registerTrainee(Trainee trainee);

    Optional<Trainee> searchTrainee(Long traineeId);

    void updateTrainee(Trainee trainee);

    void deleteTrainee(Long traineeId);

    List<Trainee> getAllTrainees();
}
