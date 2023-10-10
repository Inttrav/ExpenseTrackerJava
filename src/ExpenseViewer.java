import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;


public class ExpenseViewer extends JFrame {

    private JTable expenseTable;
    private JScrollPane scrollPane;

    public ExpenseViewer() {
        setTitle("Expense Viewer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a table model to display expenses
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Date");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Category");

        expenseTable = new JTable(tableModel);
        scrollPane = new JScrollPane(expenseTable);

        // Fetch expenses from the database and populate the table
        fetchAndDisplayExpenses();

        add(scrollPane);
        setLocationRelativeTo(null);
    }

    // Fetch expenses from the database and populate the table
    private void fetchAndDisplayExpenses() {
        String url = "jdbc:sqlite:C:/Users/aarav/IdeaProjects/first applet/src/users.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT expense_date, amount, category_name FROM expenses " +
                    "INNER JOIN categories ON expenses.category_id = categories.category_id";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel tableModel = (DefaultTableModel) expenseTable.getModel();
            tableModel.setRowCount(0); // Clear existing rows

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpenseViewer expenseViewer = new ExpenseViewer();
            expenseViewer.setVisible(true);
        });
    }
}