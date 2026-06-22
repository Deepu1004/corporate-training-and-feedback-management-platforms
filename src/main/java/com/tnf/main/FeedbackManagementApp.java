package com.tnf.training.main;

import com.tnf.training.entity.Feedback;
import com.tnf.training.reports.ReportService;
import com.tnf.training.reports.ReportServiceImpl;
import com.tnf.training.reports.TrainerRating;
import com.tnf.training.service.FeedbackService;
import com.tnf.training.service.FeedbackServiceImpl;
import com.tnf.training.util.HibernateUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

// Console driver for the Feedback & Reports module (Group 4). Wires the service and report layers into a menu.
public class FeedbackManagementApp {

    private static final FeedbackService feedbackService = new FeedbackServiceImpl();
    private static final ReportService reportService = new ReportServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            switch (readInt("Choice: ")) {
                case 1 -> submitFeedback();
                case 2 -> printFeedback(feedbackService.getAllFeedback());
                case 3 -> printFeedback(feedbackService.getFeedbackByTrainer(readLong("Trainer id: ")));
                case 4 -> printFeedback(feedbackService.getFeedbackByBatch(readLong("Batch id: ")));
                case 5 -> showAverageRating();
                case 6 -> printTrainer("Best trainer", reportService.getBestTrainer());
                case 7 -> printTrainer("Lowest rated trainer", reportService.getLowestRatedTrainer());
                case 8 -> System.out.println("Trainings conducted: " + reportService.getTrainingsConducted(readLong("Trainer id: ")));
                case 0 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        HibernateUtil.shutdown();
        System.out.println("Bye.");
    }

    // Show the main menu.
    private static void printMenu() {
        System.out.println("""

                === Feedback & Reports ===
                1. Submit Feedback
                2. View All Feedback
                3. Trainer-wise Feedback
                4. Batch-wise Feedback
                5. Average Rating
                6. Best Trainer
                7. Lowest Rated Trainer
                8. Trainings Conducted (by trainer)
                0. Exit""");
    }

    // Collect input and submit one feedback.
    private static void submitFeedback() {
        try {
            Long traineeId = readLong("Trainee id: ");
            Long trainerId = readLong("Trainer id: ");
            Long batchId = readLong("Batch id: ");
            int rating = readInt("Rating (1-5): ");
            System.out.print("Comments: ");
            String comments = scanner.nextLine();
            Feedback saved = feedbackService.submitFeedback(traineeId, trainerId, batchId, rating, comments);
            System.out.println("Saved feedback id " + saved.getFeedbackId());
        } catch (RuntimeException ex) {
            System.out.println("Could not submit: " + ex.getMessage());
        }
    }

    // Print the overall average rating.
    private static void showAverageRating() {
        Double average = reportService.getAverageRating();
        System.out.println(average == null ? "No feedback yet." : "Average rating: " + average);
    }

    // Print a list of feedback, or a note if empty.
    private static void printFeedback(List<Feedback> feedback) {
        if (feedback.isEmpty()) {
            System.out.println("No feedback found.");
            return;
        }
        feedback.forEach(System.out::println);
    }

    // Print a trainer-rating report line, or a note if there is no data.
    private static void printTrainer(String label, Optional<TrainerRating> rating) {
        if (rating.isEmpty()) {
            System.out.println(label + ": no data.");
            return;
        }
        TrainerRating r = rating.get();
        System.out.println(label + ": " + r.trainerName() + " (id " + r.trainerId() + ", avg " + r.averageRating() + ")");
    }

    // Read an int, reprompting on bad input.
    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Enter a number. " + prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume the trailing newline
        return value;
    }

    // Read a long, reprompting on bad input.
    private static long readLong(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextLong()) {
            scanner.next();
            System.out.print("Enter a number. " + prompt);
        }
        long value = scanner.nextLong();
        scanner.nextLine(); // consume the trailing newline
        return value;
    }
}
