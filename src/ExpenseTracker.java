import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ExpenseTracker extends JFrame {

    private final JTextField dateField;
    private final JTextField amountField;
    private final JComboBox<String> categoryDropdown;
    private final JTable expenseTable;

    public ExpenseTracker() {
        setTitle("Expense Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateField = new JTextField(10);

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(10);

        JLabel categoryLabel = new JLabel("Category:");
        categoryDropdown = new JComboBox<>();

        JButton addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String category = categoryDropdown.getSelectedItem().toString();

                if (addExpenseToDatabase(date, amount, category)) {
                    JOptionPane.showMessageDialog(ExpenseTracker.this, "Expense added successfully!");
                    clearInputFields();
                    fetchAndDisplayExpenses();
                } else {
                    JOptionPane.showMessageDialog(ExpenseTracker.this, "Failed to add expense.");
                }
            }
        });

        JButton createCategoryButton = new JButton("Create New Category");
        createCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newCategory = JOptionPane.showInputDialog(ExpenseTracker.this, "Enter new category:");
                if (newCategory != null && !newCategory.trim().isEmpty()) {
                    if (createCategoryInDatabase(newCategory)) {
                        populateCategoryDropdown();
                        categoryDropdown.setSelectedItem(newCategory);
                    } else {
                        JOptionPane.showMessageDialog(ExpenseTracker.this, "Failed to create category.");
                    }
                }
            }
        });

        JButton viewExpensesButton = new JButton("View Expenses");
        viewExpensesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openExpenseViewer();
            }
        });

        // Table to display expenses
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Date");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Category");
        expenseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(expenseTable);

        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(categoryLabel);
        panel.add(categoryDropdown);
        panel.add(addExpenseButton);
        panel.add(createCategoryButton);
        panel.add(viewExpensesButton);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);

        // Populate the category dropdown initially
        populateCategoryDropdown();
        fetchAndDisplayExpenses();
    }

    private boolean addExpenseToDatabase(String date, double amount, String category) {
        String url = "jdbc:sqlite:C:/Users/aarav/IdeaProjects/first applet/src/users.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            int categoryId = getCategoryIDByName(category);

            if (categoryId != -1) {
                String sql = "INSERT INTO expenses (expense_date, amount, category_id) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, date);
                statement.setDouble(2, amount);
                statement.setInt(3, categoryId);

                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean createCategoryInDatabase(String category) {
        String url = "jdbc:sqlite:C:/Users/aarav/IdeaProjects/first applet/src/users.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO categories (category_name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, category);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private int getCategoryIDByName(String categoryName) {
        String url = "jdbc:sqlite:C:/Users/aarav/IdeaProjects/first applet/src/users.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT category_id FROM categories WHERE category_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, categoryName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("category_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private void clearInputFields() {
        dateField.setText("");
        amountField.setText("");
    }

    private void populateCategoryDropdown() {
        categoryDropdown.removeAllItems();

        String url = "jdbc:sqlite:C:/Users/aarav/IdeaProjects/first applet/src/users.db";
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT category_name FROM categories";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String categoryName = resultSet.getString("category_name");
                categoryDropdown.addItem(categoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchAndDisplayExpenses() {
        DefaultTableModel tableModel = (DefaultTableModel) expenseTable.getModel();
        tableModel.setRowCount(0);

        String url = "jdbc:sqlite:C:/Users/aarav/IdeaProjects/first applet/src/users.db";
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT expense_date, amount, category_name FROM expenses " +
                    "INNER JOIN categories ON expenses.category_id = categories.category_id";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String date = resultSet.getString("expense_date");
                double amount = resultSet.getDouble("amount");
                String category = resultSet.getString("category_name");

                tableModel.addRow(new Object[]{date, amount, category});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openExpenseViewer() {
        ExpenseViewer expenseViewer = new ExpenseViewer();
        expenseViewer.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpenseTracker expenseTracker = new ExpenseTracker();
            expenseTracker.setVisible(true);
        });
    }
}
