package Project;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BudgetTracker {
    private List<Expense> expenses;
    private double totalBudget;
    private double savingsTarget;

    public BudgetTracker(double totalBudget, double savingsTarget) {
        this.expenses = new ArrayList<>();
        this.totalBudget = Math.max(totalBudget, 0); // Ensure non-negative budget
        this.savingsTarget = Math.max(savingsTarget, 0); // Ensure non-negative savings target
    }

    // Method to add an expense
    public void addExpense(String category, double amount, String description) {
        if (amount < 0) {
            System.out.println("Expense amount cannot be negative.");
            return; // Early exit for invalid amounts
        }
        Expense newExpense = new Expense(category, amount, description);
        expenses.add(newExpense);
    }

    // Calculate total expenses
    public double calculateTotalExpenses() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    // Generate the budget report
    public String generateReport() {
        DecimalFormat df = new DecimalFormat("#.00");
        double totalExpenses = calculateTotalExpenses();
        StringBuilder report = new StringBuilder();
        
        report.append("===== Budget Report =====\n")
              .append("Total Budget: INR ").append(df.format(totalBudget))
              .append("\nSavings Target: INR ").append(df.format(savingsTarget))
              .append("\nTotal Expenses: INR ").append(df.format(totalExpenses))
              .append("\n----------------------------\n");

        // Remaining budget calculation
        double remainingBudget = totalBudget - totalExpenses;
        report.append("Remaining Budget: INR ").append(df.format(remainingBudget));

        // Savings target analysis
        if (remainingBudget >= savingsTarget) {
            report.append("\nCongratulations! You've met your savings target of INR ").append(df.format(savingsTarget));
        } else {
            report.append("\nYou are INR ").append(df.format(savingsTarget - remainingBudget))
                  .append(" away from your savings target.");
        }

        // Check if budget has been exceeded
        if (totalExpenses > totalBudget) {
            report.append("\n\nYou've exceeded your budget by INR ").append(df.format(totalExpenses - totalBudget));
            report.append("\nRecommendations to reduce spending:");
            report.append(showSavingsSuggestions());
        } else {
            report.append("\nYou're within your budget. Keep up the good work!");
        }

        return report.toString();
    }

    // Method to return all expenses with details
    public String showAllExpenses() {
        if (expenses.isEmpty()) {
            return "No expenses recorded.";
        }

        DecimalFormat df = new DecimalFormat("#.00");
        StringBuilder expenseDetails = new StringBuilder("===== All Expenses =====\n");
        for (Expense e : expenses) {
            expenseDetails.append("Category: ").append(e.getCategory())
                          .append(", Amount: INR ").append(df.format(e.getAmount()))
                          .append(", Description: ").append(e.getDescription())
                          .append("\n");
        }
        return expenseDetails.toString();
    }

    // Provide suggestions for reducing spending
    private String showSavingsSuggestions() {
        StringBuilder suggestions = new StringBuilder();
        for (Expense e : expenses) {
            String category = e.getCategory().toLowerCase();
            switch (category) {
                case "dining out":
                    suggestions.append("\n- Reduce dining out expenses.");
                    break;
                case "shopping":
                    suggestions.append("\n- Cut down on shopping.");
                    break;
                case "entertainment":
                    suggestions.append("\n- Limit entertainment spending.");
                    break;
                case "travel":
                    suggestions.append("\n- Consider cutting down on travel expenses.");
                    break;
                // Add other categories as needed
                default:
                    suggestions.append("\n- Review spending in ").append(e.getCategory()).append(".");
                    break;
            }
        }
        return suggestions.toString();
    }
}
