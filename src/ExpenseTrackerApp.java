import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpenseTrackerApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Create the main JFrame
        JFrame frame = new JFrame("Expense Tracker App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new FlowLayout());

        // Create a panel to hold the buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        // Create the "Login" button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the login window when the "Login" button is clicked
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
            }
        });

        // Create the "Register" button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the registration window when the "Register" button is clicked
                RegistrationApp registrationApp = new RegistrationApp();
                registrationApp.setVisible(true);
            }
        });

        // Add buttons to the panel
        panel.add(loginButton);
        panel.add(registerButton);

        // Add the panel to the frame
        frame.add(panel);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Make the frame visible
        frame.setVisible(true);
    }
}
