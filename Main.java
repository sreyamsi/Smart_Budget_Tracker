package Project;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create a BudgetTracker object with a specified budget
        System.out.println("Welcome to the Smart Budget Tracker!");
        
        double budget = getInput(scanner, "Enter your total budget:");
        double savingsTarget = getInput(scanner, "Enter your savings target:");

        BudgetTracker budgetTracker = new BudgetTracker(budget, savingsTarget);

        // Adding expenses
        while (true) {
            System.out.print("Add an expense? (yes/no): ");
            String response = scanner.next();
            if (response.equalsIgnoreCase("no")) {
                break;
            }

            // Clear the buffer
            scanner.nextLine();

            String category = getStringInput(scanner, "Enter category (e.g., Dining Out, Shopping, Entertainment):");
            double amount = getInput(scanner, "Enter amount:");
            String description = getStringInput(scanner, "Enter description:");

            budgetTracker.addExpense(category, amount, description);
            System.out.println("Expense added successfully!\n");
        }

        // Generate and display report
        String report = budgetTracker.generateReport();
        System.out.println(report);

        scanner.close();
    }

    // Helper method to get valid double input
    private static double getInput(Scanner scanner, String prompt) {
        double value = -1;
        while (value < 0) {
            System.out.print(prompt + " ");
            try {
                value = Double.parseDouble(scanner.nextLine());
                if (value < 0) {
                    System.out.println("Please enter a non-negative number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return value;
    }

    // Helper method to get valid string input
    private static String getStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }
}
