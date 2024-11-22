package tracker;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetTracker {
    private String username;
    private List<Expense> expenses;

    public BudgetTracker(String username) {
        this.username = username;
        this.expenses = new ArrayList<>();
        loadExpenses(); // Load user's specific expenses from file
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    public class Expense {
        private String category;
        private double amount;
        private String username;

        public Expense(String category, double amount, String username) {
            this.category = category;
            this.amount = amount;
            this.username = username;
        }

        public String getCategory() {
            return category;
        }

        public double getAmount() {
            return amount;
        }

        public String getUsername() {
            return username;
        }
    }

    // Load expenses from a file specific to the username
    private void loadExpenses() {
        File file = new File(username + "_expenses.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] expenseDetails = line.split(",");
                    if (expenseDetails.length == 3) {
                        String category = expenseDetails[0].trim();
                        double amount = Double.parseDouble(expenseDetails[1].trim());
                        String user = expenseDetails[2].trim();
                        // Only load expenses that belong to the current user
                        if (user.equals(username)) {
                            expenses.add(new Expense(category, amount, user));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Get the list of expenses for the current user
    public List<Expense> getExpenses() {
        return expenses;
    }

    // Add an expense and save it to the file
    public void addExpense(String category, double amount) {
        Expense expense = new Expense(category, amount, username);
        expenses.add(expense);
        saveExpense(expense);
    }

    // Save the expense to the file (username_expenses.txt)
    private void saveExpense(Expense expense) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(username + "_expenses.txt", true))) {
            writer.write(expense.getCategory() + "," + expense.getAmount() + "," + expense.getUsername() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
