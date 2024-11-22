package tracker;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShowExpensesDialog extends JDialog {
    public ShowExpensesDialog(JFrame parent, BudgetTracker budgetTracker) {
        super(parent, "Show All Expenses", true); // Modal dialog
        // Setting dialog properties
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Create a JTextArea to show the expenses
        JTextArea expensesArea = new JTextArea();
        expensesArea.setEditable(false);
        expensesArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // Retrieve all expenses and display them
        List<BudgetTracker.Expense> expenses = budgetTracker.getExpenses();
        StringBuilder sb = new StringBuilder();
        for (BudgetTracker.Expense expense : expenses) {
            sb.append(expense.getCategory())
                    .append(": $")
                    .append(expense.getAmount())
                    .append("\n");
        }

        expensesArea.setText(sb.toString());

        // Add the JTextArea to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(expensesArea);
        add(scrollPane, BorderLayout.CENTER);

        // Add a button to close the dialog
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(_ -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
