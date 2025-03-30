package test2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerViewScreen extends JFrame {
    private JTextField searchField;
    private JTable customerTable;

    public CustomerViewScreen() {
        setTitle("View Customers");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // Adjusted layout for search field and button
        searchPanel.add(new JLabel("Search Customers (by ID or Name):"));
        searchField = new JTextField();
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(0, 123, 255)); // Blue color
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        searchButton.addActionListener(e -> searchCustomers());
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        // Result Area (JTable)
        String[] columnNames = {"Customer ID", "Name", "Address", "Contact"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back Button Panel
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Align to the left
        backButtonPanel.setBackground(new Color(240, 248, 255)); // Light blue background

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(new Color(173, 216, 230)); // Light blue color
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.setPreferredSize(new Dimension(100, 30)); // Smaller back button
        backButton.addActionListener(e -> dispose()); // Close the current screen
        backButtonPanel.add(backButton);

        add(backButtonPanel, BorderLayout.SOUTH);

        // Load all customers initially
        loadAllCustomers(tableModel);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void loadAllCustomers(DefaultTableModel tableModel) {
        String query = "SELECT * FROM Customers";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] rowData = {
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("contact")
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchCustomers() {
        String searchTerm = searchField.getText();

        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a search term!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "SELECT * FROM Customers WHERE customer_id = ? OR name LIKE ?";
        DefaultTableModel tableModel = (DefaultTableModel) customerTable.getModel();
        tableModel.setRowCount(0); // Clear existing rows

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (isNumeric(searchTerm)) {
                stmt.setInt(1, Integer.parseInt(searchTerm));
                stmt.setString(2, "%");
            } else {
                stmt.setInt(1, -1); // Invalid ID to skip numeric search
                stmt.setString(2, "%" + searchTerm + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] rowData = {
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("contact")
                };
                tableModel.addRow(rowData);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No customers found!", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}