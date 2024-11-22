package tracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BudgetSetupPage extends JFrame {
    private JTextField totalIncomeField, depositsField, emisField, savingsTargetField;
    private JButton saveButton, editButton, nextButton; // Added next button
    private String username;

    public BudgetSetupPage(String username) {
        this.username = username;

        setTitle("Budget Setup");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel with Border and Padding
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10)); // Updated to 7 rows for the new button
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Add border to the panel
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        JLabel totalIncomeLabel = new JLabel("Total Income:");
        totalIncomeField = new JTextField(15);
        JLabel depositsLabel = new JLabel("Deposits:");
        depositsField = new JTextField(15);
        JLabel emisLabel = new JLabel("EMIs:");
        emisField = new JTextField(15);
        JLabel savingsTargetLabel = new JLabel("Savings Target:");
        savingsTargetField = new JTextField(15);
        saveButton = new JButton("Save");
        editButton = new JButton("Edit");
        nextButton = new JButton("Next"); // Next button

        panel.add(totalIncomeLabel);
        panel.add(totalIncomeField);
        panel.add(depositsLabel);
        panel.add(depositsField);
        panel.add(emisLabel);
        panel.add(emisField);
        panel.add(savingsTargetLabel);
        panel.add(savingsTargetField);
        panel.add(saveButton);
        panel.add(editButton);

        // Creating a JPanel to hold the next button and align it at the bottom
        JPanel nextPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center alignment
        nextPanel.add(nextButton);

        // Main layout to add both the form and nextPanel
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(nextPanel, BorderLayout.SOUTH); // Place the Next button at the bottom

        // Load existing budget setup for the user
        loadBudgetSetup();

        // Save button action listener
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String totalIncome = totalIncomeField.getText();
                String deposits = depositsField.getText();
                String emis = emisField.getText();
                String savingsTarget = savingsTargetField.getText();

                if (totalIncome.isEmpty() || deposits.isEmpty() || emis.isEmpty() || savingsTarget.isEmpty()) {
                    JOptionPane.showMessageDialog(BudgetSetupPage.this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    saveBudgetSetup(totalIncome, deposits, emis, savingsTarget);
                    JOptionPane.showMessageDialog(BudgetSetupPage.this, "Budget details saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    editButton.setEnabled(true); // Allow editing after saving
                    saveButton.setEnabled(false); // Disable save after saving
                    editButton.setText("Edit"); // Change text back to "Edit"
                }
            }
        });

        // Edit button action listener
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle between "Edit" and "Done" states
                if (editButton.getText().equals("Edit")) {
                    enableEditing(); // Enable fields for editing
                    editButton.setText("Done"); // Change button text to "Done"
                } else {
                    saveButton.doClick(); // Save the data when "Done" is clicked
                }
            }
        });

        // Next button action listener
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to the main frame
                new MainFrame(username); // Pass the username to MainFrame
                dispose(); // Close the current page
            }
        });

        // Initially disable editing
        disableEditing();

        setVisible(true);
    }

    private void loadBudgetSetup() {
        try (BufferedReader reader = new BufferedReader(new FileReader(username + "_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                totalIncomeField.setText(data[0]);
                depositsField.setText(data[1]);
                emisField.setText(data[2]);
                savingsTargetField.setText(data[3]);
                saveButton.setEnabled(false); // Disable save after loading data
                editButton.setEnabled(true); // Enable edit after loading data
                editButton.setText("Edit"); // Ensure "Edit" is the initial text
            }
        } catch (IOException e) {
            // No data for new user, continue
            saveButton.setEnabled(true);
            editButton.setEnabled(true); // Enable the edit button for new users
            editButton.setText("Edit"); // Make sure the text is "Edit" for a new user
        }
    }

    private void saveBudgetSetup(String totalIncome, String deposits, String emis, String savingsTarget) {
        try (FileWriter writer = new FileWriter(username + "_details.txt")) {
            writer.write(totalIncome + "," + deposits + "," + emis + "," + savingsTarget);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enableEditing() {
        // Enable text fields and the save button for editing
        totalIncomeField.setEditable(true);
        depositsField.setEditable(true);
        emisField.setEditable(true);
        savingsTargetField.setEditable(true);
        saveButton.setEnabled(true); // Enable save button
    }

    private void disableEditing() {
        // Disable text fields initially
        totalIncomeField.setEditable(false);
        depositsField.setEditable(false);
        emisField.setEditable(false);
        savingsTargetField.setEditable(false);
        saveButton.setEnabled(true); // Enable save button for first-time data entry
    }

    public static void main(String[] args) {
        new BudgetSetupPage("testuser"); // Example username
    }
}
