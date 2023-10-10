import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationApp extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegistrationApp() {
        setTitle("Registration");
        setSize(300, 150);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Register the user and open the login window upon successful registration
                if (registerUser(username, password)) {
                    JOptionPane.showMessageDialog(RegistrationApp.this, "Registration successful!");


                    openLoginWindow();

                    dispose(); // Close the RegistrationApp window.
                } else {
                    JOptionPane.showMessageDialog(RegistrationApp.this, "Registration failed.");
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);

        add(panel);
        setLocationRelativeTo(null);
    }

    private boolean registerUser(String username, String password) {
        String url = "jdbc:sqlite:C:/Users/aarav/IdeaProjects/first applet/src/users.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Function to open the LoginWindow
    private void openLoginWindow() {
        SwingUtilities.invokeLater(() -> {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistrationApp registrationApp = new RegistrationApp();
            registrationApp.setVisible(true);
        });
    }
}
