package com.tnf.dao;
import java.util.List;

import com.tnf.entities.Trainee;


public interface TraineeDAO {

    void registerTrainee(Trainee trainee);

    Trainee findTrainee(Long traineeId);

    void updateTrainee(Trainee trainee);

    void deleteTrainee(Long traineeId);

    List<Trainee> getAllTrainees();
}
