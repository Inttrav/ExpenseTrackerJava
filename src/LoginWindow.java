import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginWindow() {
        setTitle("Login");
        setSize(300, 150);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check login details against the database
                if (validateLogin(username, password)) {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Login successful!");

                    // Open the ExpenseTracker window upon successful login
                    openExpenseTracker();

                    dispose(); // Close the LoginWindow.
                } else {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Login failed. Please check your credentials.");
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        add(panel);
        setLocationRelativeTo(null);
    }

    // Function to validate login credentials against the SQLite database
    private boolean validateLogin(String username, String password) {
        String url = "jdbc:sqlite:C:/Users/aarav/IdeaProjects/first applet/src/users.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If a matching record is found, return true.
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Function to open the ExpenseTracker window
    private void openExpenseTracker() {
        SwingUtilities.invokeLater(() -> {
            ExpenseTracker expenseTracker = new ExpenseTracker();
            expenseTracker.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
        });
    }
}
