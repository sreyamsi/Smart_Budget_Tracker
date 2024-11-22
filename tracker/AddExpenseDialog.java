package tracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddExpenseDialog extends JDialog {
    private JTextField categoryField;
    private JTextField amountField;
    // Constructor for initializing the dialog
    public AddExpenseDialog(Frame owner, BudgetTracker budgetTracker) {
        super(owner, "Add Expense", true);  // True makes this modal
        setSize(300, 200);
        setLocationRelativeTo(owner);  // Center the dialog on the parent frame

        // Create the main panel and set its layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));  // Adding spacing between components
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Adding padding around the panel

        // Set a uniform font for all components
        Font font = new Font("Arial", Font.PLAIN, 14);

        // Labels and text fields for category and amount
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(font);
        categoryField = new JTextField(15);
        categoryField.setFont(font);  // Set font for the text field

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(font);
        amountField = new JTextField(15);
        amountField.setFont(font);  // Set font for the text field

        // Button to add expense
        JButton addButton = new JButton("Add Expense");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));  // Bold button text
        addButton.setBackground(new Color(34, 167, 240));  // Set button color
        addButton.setForeground(Color.WHITE);  // Set text color to white
        addButton.setFocusPainted(false);  // Remove the focus border
        addButton.setBorder(BorderFactory.createRaisedBevelBorder());  // Add border to the button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from input fields
                String category = categoryField.getText().trim();  // Trim whitespace from category
                if (category.isEmpty()) {
                    JOptionPane.showMessageDialog(AddExpenseDialog.this, "Category cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;  // Prevent adding an expense with an empty category
                }

                try {
                    double amount = Double.parseDouble(amountField.getText().trim());  // Trim and parse amount
                    if (amount <= 0) {
                        throw new NumberFormatException();  // Handle negative or zero amounts
                    }

                    // Add the expense to the budget tracker
                    budgetTracker.addExpense(category, amount);
                    JOptionPane.showMessageDialog(AddExpenseDialog.this, "Expense added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();  // Close dialog after adding the expense
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AddExpenseDialog.this, "Invalid or negative amount entered!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the panel
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(new JLabel());  // Empty space to align the button correctly
        panel.add(addButton);

        // Add the panel to the dialog window
        add(panel);
    }
}
