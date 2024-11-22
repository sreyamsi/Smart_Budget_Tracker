package tracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class SignUpPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignUpPage() {
        setTitle("Sign Up");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Grid layout for fields and buttons
        JPanel gridPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(15);
        JButton signUpButton = new JButton("Sign Up");
        JButton loginButton = new JButton("Login");

        // Add components to grid panel
        gridPanel.add(usernameLabel);
        gridPanel.add(usernameField);
        gridPanel.add(passwordLabel);
        gridPanel.add(passwordField);
        gridPanel.add(confirmPasswordLabel);
        gridPanel.add(confirmPasswordField);
        gridPanel.add(signUpButton);
        gridPanel.add(loginButton);

        // Padding around the grid panel
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Add grid panel to frame
        setLayout(new BorderLayout());
        add(gridPanel, BorderLayout.CENTER);

        // Sign Up button action listener
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(SignUpPage.this, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(SignUpPage.this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    saveUser(username, password);
                    JOptionPane.showMessageDialog(SignUpPage.this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new LoginPage().setVisible(true); // Open login page
                    dispose(); // Close the sign-up page
                }
            }
        });

        // Login button action listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true); // Open login page directly
                dispose(); // Close the sign-up page
            }
        });

        setVisible(true);
    }

    private void saveUser(String username, String password) {
        try (FileWriter fw = new FileWriter("users.txt", true)) {
            fw.write(username + "," + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SignUpPage(); // Open sign-up page
    }
}
