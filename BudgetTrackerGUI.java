package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BudgetTrackerGUI extends JFrame {
    private BudgetTracker budgetTracker;
    private JTextField userNameField, totalIncomeField, savingsField, expectationAmountField, incomeSourceField, fixedExpensesField;
    private JComboBox<String> familyMembersDropdown, timePeriodDropdown;
    private JTextField categoryField, amountField, descriptionField;
    private JTextArea outputArea;
    private JPanel initialPanel, expensePanel;

    public BudgetTrackerGUI() {
        setTitle("Smart Budget Tracker");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        // Initialize Panels
        initialPanel = createInitialPanel();
        expensePanel = createExpensePanel();

        // Add both panels to the frame
        add(initialPanel, "Initial");
        add(expensePanel, "Expense");

        // Show the initial panel
        CardLayout layout = (CardLayout) getContentPane().getLayout();
        layout.show(getContentPane(), "Initial");

        setVisible(true);
        setLocationRelativeTo(null);
    }

    // Create Initial Panel
    private JPanel createInitialPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userNameField = addField(panel, "User Name:", gbc, 0);
        totalIncomeField = addField(panel, "Total Income:", gbc, 1);
        savingsField = addField(panel, "Savings Target:", gbc, 2);
        fixedExpensesField = addField(panel, "Fixed Expenses:", gbc, 3);
        
        String[] familyMembersOptions = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        familyMembersDropdown = new JComboBox<>(familyMembersOptions);
        addLabelAndComponent(panel, "Number of Family Members:", familyMembersDropdown, gbc, 4);
        
        incomeSourceField = addField(panel, "Income Source:", gbc, 5);
        timePeriodDropdown = new JComboBox<>(new String[]{"Day", "Month", "3 Months", "6 Months", "Yearly"});
        addLabelAndComponent(panel, "Time Period:", timePeriodDropdown, gbc, 6);

        expectationAmountField = new JTextField(10);
        expectationAmountField.setEditable(false); // Make this field read-only
        addLabelAndComponent(panel, "Expectation Amount:", expectationAmountField, gbc, 7);
        
        JButton calculateButton = createButton("Calculate", e -> calculateExpectationAmount());
        JButton nextButton = createButton("Next", e -> {
            if (validateFields()) {
                initializeTracker();
                showExpensePanel();
            }
        });

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateButton);
        buttonPanel.add(nextButton);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    // Create Expense Panel
    // Create Expense Panel
private JPanel createExpensePanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Add Expenses section at the top
    JPanel addExpensePanel = new JPanel();
    addExpensePanel.setLayout(new GridLayout(3, 2));
    
    categoryField = new JTextField(15);
    amountField = new JTextField(15);
    descriptionField = new JTextField(15);

    addExpensePanel.add(new JLabel("Category: "));
    addExpensePanel.add(categoryField);
    
   
    addExpensePanel.add(new JLabel("Amount: "));
    addExpensePanel.add(amountField);
    addExpensePanel.add(new JLabel("Description: "));
    addExpensePanel.add(descriptionField);

    JButton addExpenseButton = createButton("Add Expense", e -> addExpense());

    // Layout adjustments for the button
    JPanel addExpenseButtonPanel = new JPanel();
    addExpenseButtonPanel.add(addExpenseButton);

    panel.add(addExpensePanel);
    panel.add(addExpenseButtonPanel);

    // Buttons for showing expenses and recommendations
    JButton showRecButton = createButton("Show Recommendations", e -> showRecommendations());
    JButton showAllExpensesButton = createButton("Show All Expenses", e -> showAllExpenses());

    // Layout adjustments for expense buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 2));
    buttonPanel.add(showAllExpensesButton);
    buttonPanel.add(showRecButton);

    outputArea = new JTextArea(10, 30);
    outputArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(outputArea);

    panel.add(Box.createRigidArea(new Dimension(0, 20)));
    panel.add(buttonPanel);
    panel.add(scrollPane);

    return panel;
}


    // Add field to panel
    private JTextField addField(JPanel panel, String label, GridBagConstraints gbc, int row) {
        JTextField field = new JTextField(20);
        addLabelAndComponent(panel, label, field, gbc, row);
        return field;
    }

    // Add label and component to panel
    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);
        
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    // Create button with action
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    // Validate the required fields on the first page
    private boolean validateFields() {
        if (userNameField.getText().isEmpty() || totalIncomeField.getText().isEmpty() || savingsField.getText().isEmpty() ||
            fixedExpensesField.getText().isEmpty() || incomeSourceField.getText().isEmpty()) {
            showErrorDialog("Please fill in all required fields.");
            return false;
        }
        return true;
    }

    // Show error dialog
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Calculate the expectation amount based on inputs
    private void calculateExpectationAmount() {
        try {
            double totalIncome = parsePositiveDouble(totalIncomeField.getText(), "Total Income");
            double savingsTarget = parsePositiveDouble(savingsField.getText(), "Savings Target");
            double fixedExpenses = parsePositiveDouble(fixedExpensesField.getText(), "Fixed Expenses");
            int familyMembers = Integer.parseInt((String) familyMembersDropdown.getSelectedItem());

            // New formula for expectation amount with fixed expenses
            double estimation = (totalIncome - savingsTarget - fixedExpenses) / familyMembers;

            expectationAmountField.setText(String.format("%.2f", estimation));
            JOptionPane.showMessageDialog(this, "Calculated Expectation Amount: " + estimation, "Calculation Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid number format.");
        }
    }

    // Parse positive double
    private double parsePositiveDouble(String text, String fieldName) throws NumberFormatException {
        double value = Double.parseDouble(text);
        if (value < 0) {
            throw new NumberFormatException(fieldName + " must be non-negative.");
        }
        return value;
    }

    // Initialize BudgetTracker
    private void initializeTracker() {
        double totalIncome = Double.parseDouble(totalIncomeField.getText());
        double savingsTarget = Double.parseDouble(savingsField.getText());
        // double fixedExpenses = Double.parseDouble(fixedExpensesField.getText());
        budgetTracker = new BudgetTracker(totalIncome, savingsTarget); // Consider how fixedExpenses fits in your BudgetTracker class
    }

    // Show expense panel
    private void showExpensePanel() {
        CardLayout layout = (CardLayout) getContentPane().getLayout();
        layout.show(getContentPane(), "Expense");
    }

    // Add an expense
    private void addExpense() {
        if (budgetTracker == null) {
            showErrorDialog("Please enter your budget and savings target first.");
            return;
        }

        String category = categoryField.getText();
        double amount;
        try {
            amount = parsePositiveDouble(amountField.getText(), "Amount");
        } catch (NumberFormatException e) {
            showErrorDialog("Please enter a valid amount.");
            return;
        }
        String description = descriptionField.getText();

        budgetTracker.addExpense(category, amount, description);
        JOptionPane.showMessageDialog(this, "Expense added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields
        categoryField.setText("");
        amountField.setText("");
        descriptionField.setText("");
    }

    // Show recommendations
    private void showRecommendations() {
        if (budgetTracker != null) {
            String recommendations = budgetTracker.generateReport();
            outputArea.setText(recommendations);
        } else {
            showErrorDialog("Please enter your budget and savings target first.");
        }
    }

    // Show all expenses
    private void showAllExpenses() {
        if (budgetTracker != null) {
            String allExpenses = budgetTracker.showAllExpenses();
            outputArea.setText(allExpenses);
        } else {
            showErrorDialog("Please enter your budget and savings target first.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetTrackerGUI::new);
    }
}
