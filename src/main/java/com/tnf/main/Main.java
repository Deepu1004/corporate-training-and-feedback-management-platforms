package com.tnf.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.tnf.dao.BatchDAO;
import com.tnf.entity.Batch;
import com.tnf.entity.Feedback;
import com.tnf.entity.Trainee;
import com.tnf.entity.Trainer;
import com.tnf.exception.TraineeNotFoundException;
import com.tnf.reports.ReportService;
import com.tnf.reports.ReportServiceImpl;
import com.tnf.reports.TrainerRating;
import com.tnf.service.FeedbackService;
import com.tnf.service.FeedbackServiceImpl;
import com.tnf.service.TraineeService;
import com.tnf.service.TraineeServiceImpl;
import com.tnf.service.TrainerService;
import com.tnf.util.HibernateUtil;

// Single CLI test harness that wires together every module (Trainer, Batch, Trainee, Feedback & Reports).
// One Scanner and one Hibernate SessionFactory are shared across all sub-menus.
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TrainerService trainerService = new TrainerService();
    private static final BatchDAO batchDAO = new BatchDAO();
    private static final TraineeService traineeService = new TraineeServiceImpl();
    private static final FeedbackService feedbackService = new FeedbackServiceImpl();
    private static final ReportService reportService = new ReportServiceImpl();

    public static void main(String[] args) {
        // Touch the SessionFactory up front so DB/connection problems surface clearly instead of mid-menu.
        try {
            HibernateUtil.getSessionFactory();
        } catch (Throwable t) {
            System.out.println("Could not start Hibernate / connect to the database.");
            System.out.println("Reason: " + rootMessage(t));
            System.out.println("Check that MySQL is running and the database in hibernate.cfg.xml exists.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("""

                    ====== Corporate Training & Feedback Platform ======
                    1. Trainer management
                    2. Batch management
                    3. Trainee management
                    4. Feedback & Reports
                    0. Exit""");
            switch (readInt("Choice: ")) {
                case 1 -> trainerMenu();
                case 2 -> batchMenu();
                case 3 -> traineeMenu();
                case 4 -> feedbackMenu();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }

        HibernateUtil.shutdown();
        scanner.close();
        System.out.println("Bye.");
    }

    // ---------------------------------------------------------------- Trainer

    private static void trainerMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("""

                    --- Trainer management ---
                    1. Add trainer
                    2. Search trainer
                    3. Update trainer
                    4. Delete trainer
                    5. View all trainers
                    0. Back""");
            switch (readInt("Choice: ")) {
                case 1 -> addTrainer();
                case 2 -> searchTrainer();
                case 3 -> updateTrainer();
                case 4 -> deleteTrainer();
                case 5 -> viewAllTrainers();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addTrainer() {
        Trainer trainer = new Trainer(
                null,
                readLine("Name: "),
                readTechnologies("Technologies (comma separated): "),
                readInt("Experience (years): "),
                readLine("Email: "),
                readLine("Mobile: "));
        try {
            Trainer saved = trainerService.registerTrainer(trainer);
            System.out.println("Saved trainer id " + saved.getId());
        } catch (RuntimeException ex) {
            System.out.println("Could not save: " + rootMessage(ex));
        }
    }

    private static void searchTrainer() {
        try {
            printTrainer(trainerService.searchTrainer(readLong("Trainer id: ")));
        } catch (RuntimeException ex) {
            System.out.println(rootMessage(ex));
        }
    }

    private static void updateTrainer() {
        long id = readLong("Trainer id to update: ");
        Trainer trainer;
        try {
            trainer = trainerService.searchTrainer(id);
        } catch (RuntimeException ex) {
            System.out.println(rootMessage(ex));
            return;
        }
        trainer.setName(readLine("Name: "));
        trainer.setTechnology(readTechnologies("Technologies (comma separated): "));
        trainer.setExperience(readInt("Experience (years): "));
        trainer.setEmail(readLine("Email: "));
        trainer.setMobile(readLine("Mobile: "));
        try {
            trainerService.updateTrainer(trainer);
            System.out.println("Trainer updated.");
        } catch (RuntimeException ex) {
            System.out.println("Could not update: " + rootMessage(ex));
        }
    }

    private static void deleteTrainer() {
        try {
            trainerService.deleteTrainer(readLong("Trainer id to delete: "));
            System.out.println("Trainer deleted.");
        } catch (RuntimeException ex) {
            System.out.println("Could not delete: " + rootMessage(ex));
        }
    }

    private static void viewAllTrainers() {
        List<Trainer> trainers = trainerService.displayAll();
        if (trainers.isEmpty()) {
            System.out.println("No trainers found.");
            return;
        }
        trainers.forEach(Main::printTrainer);
    }

    // ------------------------------------------------------------------ Batch

    private static void batchMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("""

                    --- Batch management ---
                    1. Add batch
                    2. Search batch
                    3. Update batch
                    4. Delete batch
                    5. View all batches
                    0. Back""");
            switch (readInt("Choice: ")) {
                case 1 -> addBatch();
                case 2 -> searchBatch();
                case 3 -> updateBatch();
                case 4 -> deleteBatch();
                case 5 -> viewAllBatches();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addBatch() {
        Batch batch = new Batch();
        batch.setBatchName(readLine("Batch name: "));
        batch.setTechnology(readLine("Technology: "));
        batch.setStartDate(readOptionalDate("Start date (yyyy-MM-dd, blank for none): "));
        batch.setEndDate(readOptionalDate("End date (yyyy-MM-dd, blank for none): "));
        batch.setTrainer(readTrainerRef("Trainer id (blank for none): "));
        batchDAO.saveBatch(batch);
        if (batch.getBatchId() != null) {
            System.out.println("Saved batch id " + batch.getBatchId());
        } else {
            System.out.println("Save failed (see stack trace above).");
        }
    }

    private static void searchBatch() {
        Optional<Batch> batch = batchDAO.getBatchById(readLong("Batch id: "));
        if (batch.isPresent()) {
            printBatch(batch.get());
        } else {
            System.out.println("No batch found.");
        }
    }

    private static void updateBatch() {
        long id = readLong("Batch id to update: ");
        Optional<Batch> existing = batchDAO.getBatchById(id);
        if (existing.isEmpty()) {
            System.out.println("No batch found.");
            return;
        }
        Batch batch = existing.get();
        batch.setBatchName(readLine("Batch name: "));
        batch.setTechnology(readLine("Technology: "));
        batch.setStartDate(readOptionalDate("Start date (yyyy-MM-dd, blank for none): "));
        batch.setEndDate(readOptionalDate("End date (yyyy-MM-dd, blank for none): "));
        batch.setTrainer(readTrainerRef("Trainer id (blank for none): "));
        batchDAO.updateBatch(batch);
        System.out.println("Batch updated.");
    }

    private static void deleteBatch() {
        batchDAO.deleteBatch(readLong("Batch id to delete: "));
        System.out.println("Batch deleted (if it existed).");
    }

    private static void viewAllBatches() {
        List<Batch> batches = batchDAO.getAllBatches().orElseGet(ArrayList::new);
        if (batches.isEmpty()) {
            System.out.println("No batches found.");
            return;
        }
        batches.forEach(Main::printBatch);
    }

    // ---------------------------------------------------------------- Trainee

    private static void traineeMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("""

                    --- Trainee management ---
                    1. Add trainee
                    2. Search trainee
                    3. Update trainee
                    4. Delete trainee
                    5. View all trainees
                    0. Back""");
            switch (readInt("Choice: ")) {
                case 1 -> addTrainee();
                case 2 -> searchTrainee();
                case 3 -> updateTrainee();
                case 4 -> deleteTrainee();
                case 5 -> viewAllTrainees();
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addTrainee() {
        Trainee trainee = new Trainee();
        trainee.setName(readLine("Name: "));
        trainee.setEmail(readLine("Email: "));
        trainee.setMobile(readLine("Mobile: "));
        trainee.setBatch(readBatchRef("Batch id (blank for none): "));
        try {
            traineeService.registerTrainee(trainee);
            System.out.println("Saved trainee id " + trainee.getTraineeId());
        } catch (RuntimeException ex) {
            System.out.println("Could not save: " + rootMessage(ex));
        }
    }

    private static void searchTrainee() {
        Optional<Trainee> trainee = traineeService.searchTrainee(readLong("Trainee id: "));
        if (trainee.isPresent()) {
            printTrainee(trainee.get());
        } else {
            System.out.println("No trainee found.");
        }
    }

    private static void updateTrainee() {
        Optional<Trainee> existing = traineeService.searchTrainee(readLong("Trainee id to update: "));
        if (existing.isEmpty()) {
            System.out.println("No trainee found.");
            return;
        }
        Trainee trainee = existing.get();
        trainee.setName(readLine("Name: "));
        trainee.setEmail(readLine("Email: "));
        trainee.setMobile(readLine("Mobile: "));
        trainee.setBatch(readBatchRef("Batch id (blank for none): "));
        try {
            traineeService.updateTrainee(trainee);
            System.out.println("Trainee updated.");
        } catch (TraineeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void deleteTrainee() {
        try {
            traineeService.deleteTrainee(readLong("Trainee id to delete: "));
            System.out.println("Trainee deleted.");
        } catch (TraineeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void viewAllTrainees() {
        List<Trainee> trainees = traineeService.getAllTrainees();
        if (trainees == null || trainees.isEmpty()) {
            System.out.println("No trainees found.");
            return;
        }
        trainees.forEach(Main::printTrainee);
    }

    // ------------------------------------------------------ Feedback & Reports

    private static void feedbackMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("""

                    --- Feedback & Reports ---
                    1. Submit feedback
                    2. View all feedback
                    3. Trainer-wise feedback
                    4. Batch-wise feedback
                    5. Average rating
                    6. Best trainer
                    7. Lowest rated trainer
                    8. Trainings conducted (by trainer)
                    9. Total trainings conducted
                    0. Back""");
            switch (readInt("Choice: ")) {
                case 1 -> submitFeedback();
                case 2 -> printFeedbackList(feedbackService.getAllFeedback());
                case 3 -> printFeedbackList(feedbackService.getFeedbackByTrainer(readLong("Trainer id: ")));
                case 4 -> printFeedbackList(feedbackService.getFeedbackByBatch(readLong("Batch id: ")));
                case 5 -> showAverageRating();
                case 6 -> printTrainerRating("Best trainer", reportService.getBestTrainer());
                case 7 -> printTrainerRating("Lowest rated trainer", reportService.getLowestRatedTrainer());
                case 8 -> System.out.println("Trainings conducted: "
                        + reportService.getTrainingsConducted(readLong("Trainer id: ")));
                case 9 -> System.out.println("Total trainings conducted: "
                        + reportService.getTotalTrainingsConducted());
                case 0 -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void submitFeedback() {
        try {
            Long traineeId = readLong("Trainee id: ");
            Long trainerId = readLong("Trainer id: ");
            Long batchId = readLong("Batch id: ");
            int rating = readInt("Rating (1-5): ");
            String comments = readLine("Comments: ");
            Feedback saved = feedbackService.submitFeedback(traineeId, trainerId, batchId, rating, comments);
            System.out.println("Saved feedback id " + saved.getFeedbackId());
        } catch (RuntimeException ex) {
            System.out.println("Could not submit: " + rootMessage(ex));
        }
    }

    private static void showAverageRating() {
        Double average = reportService.getAverageRating();
        System.out.println(average == null ? "No feedback yet." : "Average rating: " + average);
    }

    private static void printFeedbackList(List<Feedback> feedback) {
        if (feedback.isEmpty()) {
            System.out.println("No feedback found.");
            return;
        }
        feedback.forEach(Main::printFeedback);
    }

    private static void printTrainerRating(String label, Optional<TrainerRating> rating) {
        if (rating.isEmpty()) {
            System.out.println(label + ": no data.");
            return;
        }
        TrainerRating r = rating.get();
        System.out.println(label + ": " + r.trainerName() + " (id " + r.trainerId() + ", avg " + r.averageRating() + ")");
    }

    // ------------------------------------------------------------ print helpers
    // These only touch eagerly-loaded scalar fields so they are safe on detached
    // entities (no LazyInitializationException). technology is guarded in a try/catch.

    private static void printTrainer(Trainer t) {
        System.out.println("-----------------------------");
        System.out.println("Trainer id: " + t.getId());
        System.out.println("Name      : " + t.getName());
        System.out.println("Experience: " + t.getExperience());
        System.out.println("Email     : " + t.getEmail());
        System.out.println("Mobile    : " + t.getMobile());
        try {
            System.out.println("Technology: " + t.getTechnology());
        } catch (RuntimeException ex) {
            System.out.println("Technology: (not loaded)");
        }
    }

    private static void printBatch(Batch b) {
        System.out.println("-----------------------------");
        System.out.println("Batch id  : " + b.getBatchId());
        System.out.println("Name      : " + b.getBatchName());
        System.out.println("Technology: " + b.getTechnology());
        System.out.println("Start     : " + b.getStartDate());
        System.out.println("End       : " + b.getEndDate());
        System.out.println("Trainer id: " + (b.getTrainer() == null ? "none" : b.getTrainer().getId()));
    }

    private static void printTrainee(Trainee t) {
        System.out.println("-----------------------------");
        System.out.println("Trainee id: " + t.getTraineeId());
        System.out.println("Name      : " + t.getName());
        System.out.println("Email     : " + t.getEmail());
        System.out.println("Mobile    : " + t.getMobile());
        System.out.println("Batch id  : " + (t.getBatch() == null ? "none" : t.getBatch().getBatchId()));
    }

    private static void printFeedback(Feedback f) {
        String trainee = f.getTrainee() == null ? "?" : f.getTrainee().getName();
        String trainer = f.getTrainer() == null ? "?" : f.getTrainer().getName();
        String batch = f.getTrainingBatch() == null ? "?" : f.getTrainingBatch().getBatchName();
        System.out.println("Feedback #" + f.getFeedbackId()
                + " | rating=" + f.getRating()
                + " | trainee=" + trainee
                + " | trainer=" + trainer
                + " | batch=" + batch
                + " | comments=" + f.getComments());
    }

    // -------------------------------------------------------------- input utils

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private static long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private static Long readOptionalLong(String prompt) {
        String input = readLine(prompt);
        if (input.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, treating as none.");
            return null;
        }
    }

    private static LocalDateTime readOptionalDate(String prompt) {
        String input = readLine(prompt);
        if (input.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(input).atStartOfDay();
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date (use yyyy-MM-dd), treating as none.");
            return null;
        }
    }

    private static List<String> readTechnologies(String prompt) {
        String input = readLine(prompt);
        if (input.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    // Build a Trainer holding only its id, used as a foreign-key reference (no DB round trip).
    private static Trainer readTrainerRef(String prompt) {
        Long id = readOptionalLong(prompt);
        if (id == null) {
            return null;
        }
        Trainer trainer = new Trainer();
        trainer.setId(id);
        return trainer;
    }

    // Build a Batch holding only its id, used as a foreign-key reference (no DB round trip).
    private static Batch readBatchRef(String prompt) {
        Long id = readOptionalLong(prompt);
        if (id == null) {
            return null;
        }
        Batch batch = new Batch();
        batch.setBatchId(id);
        return batch;
    }

    // Walk to the deepest cause so DB errors aren't hidden behind generic wrapper messages.
    private static String rootMessage(Throwable t) {
        Throwable cause = t;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        String message = cause.getMessage();
        return message != null ? message : cause.getClass().getSimpleName();
    }
}
