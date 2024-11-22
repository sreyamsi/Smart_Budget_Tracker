package tracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {
    private BudgetTracker budgetTracker;

    public MainFrame(String username) {
        this.budgetTracker = new BudgetTracker(username);

        setTitle("Main Dashboard");
        setSize(600, 500); // Increased size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creating the main panel with BorderLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240)); // Light gray background for the panel

        // Slogan at the top
        JLabel sloganLabel = new JLabel("Track Your Spending, Plan Your Future!", SwingConstants.CENTER);
        sloganLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Bold and larger font for the slogan
        sloganLabel.setForeground(new Color(0, 102, 204)); // A color that contrasts well
        panel.add(sloganLabel, BorderLayout.NORTH);

        // Creating the panel for buttons with FlowLayout for spacing
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20)); // Centered buttons with a gap of 30px
        buttonPanel.setBackground(new Color(240, 240, 240)); // Background for the button panel

        // Create and style buttons
        JButton addExpenseButton = createStyledButton("Add Expense");
        JButton showExpensesButton = createStyledButton("Show All Expenses");
        JButton generateReportButton = createStyledButton("Generate Report");
        JButton logoutButton = createStyledButton("Logout");
        JButton backButton = createStyledButton("Back"); // New Back Button

        // Action listeners for buttons
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddExpenseDialog(MainFrame.this, budgetTracker).setVisible(true);
            }
        });

        showExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShowExpensesDialog(MainFrame.this, budgetTracker).setVisible(true);
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, "Logged out successfully.", "Logout", JOptionPane.INFORMATION_MESSAGE);
                redirectToLoginPage();
            }
        });

        // Action listener for the Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to the BudgetSetupPage
                new BudgetSetupPage(budgetTracker.getUsername());
                dispose(); // Close the current MainFrame
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(addExpenseButton);
        buttonPanel.add(showExpensesButton);
        buttonPanel.add(generateReportButton);
        buttonPanel.add(logoutButton);

        // Create a separate panel for the Back button and add it to the bottom
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButtonPanel.setBackground(new Color(240, 240, 240)); // Background for the bottom panel
        backButtonPanel.add(backButton);

        // Add the button panel and back button panel to the main panel
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(backButtonPanel, BorderLayout.SOUTH); // Place the back button at the bottom

        // Add the main panel to the frame
        add(panel);

        setVisible(true);
    }

    // Method to create and style buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16)); // Clean font
        button.setBackground(new Color(0, 102, 204)); // Blue background
        button.setForeground(Color.WHITE); // White text
        button.setPreferredSize(new Dimension(180, 40)); // Set a uniform size for all buttons
        button.setFocusPainted(false); // Remove focus border
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        
        // Button hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 128, 255)); // Lighter blue on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204)); // Reset to original color
            }
        });

        return button;
    }

    // Method to generate the report
    private void generateReport() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // Create a report file using the username
        File reportFile = new File(budgetTracker.getUsername() + "_report.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile))) {
            writer.write("Report Generated for " + budgetTracker.getUsername() + "\n\n");
            writer.write("Report Date: " + formattedDateTime + "\n\n");

            // Write expenses to the report
            writer.write("Expenses:\n");
            for (BudgetTracker.Expense expense : budgetTracker.getExpenses()) {
                writer.write(expense.getCategory() + ": $" + expense.getAmount() + "\n");
            }

            JOptionPane.showMessageDialog(this, "Report generated successfully!\nSaved as " + reportFile.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error generating the report!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Method to redirect to the login page
    private void redirectToLoginPage() {
        // Assuming you have a LoginPage class
        new LoginPage().setVisible(true); // Redirect to the login page
        this.dispose(); // Close the current MainFrame
    }

    // Main method to run the application
    public static void main(String[] args) {
        // Start the MainFrame with a test username
        new MainFrame("testuser");
    }
}
