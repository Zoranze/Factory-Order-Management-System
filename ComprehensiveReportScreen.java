package test2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComprehensiveReportScreen extends JFrame {
    public ComprehensiveReportScreen() {
        setTitle("Comprehensive Report");
        setSize(800, 600); // Increased size for better layout
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 123, 255));
        JLabel headerLabel = new JLabel("Comprehensive Report");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Report Area
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        add(scrollPane, BorderLayout.CENTER);

        // Load comprehensive report data
        loadComprehensiveReport(reportArea);

        // South Panel for Buttons
        JPanel southPanel = new JPanel(new BorderLayout()); // Use BorderLayout for proper alignment
        southPanel.setBackground(new Color(240, 248, 255));

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(new Color(173, 216, 230)); // Light blue color
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.setPreferredSize(new Dimension(100, 30)); // Smaller button size
        backButton.addActionListener(e -> dispose()); // Close the current screen
        southPanel.add(backButton, BorderLayout.WEST); // Align to the left

        // Button Panel (for other buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Use FlowLayout with spacing
        buttonPanel.setBackground(new Color(240, 248, 255));

        // View Sales Report Button
        JButton viewSalesReportButton = createStyledButton("View Sales Report");
        viewSalesReportButton.addActionListener(e -> navigateToSalesReport());
        buttonPanel.add(viewSalesReportButton);

        // View Inventory Report Button
        JButton viewInventoryReportButton = createStyledButton("View Inventory Report");
        viewInventoryReportButton.addActionListener(e -> navigateToInventoryReport());
        buttonPanel.add(viewInventoryReportButton);

        southPanel.add(buttonPanel, BorderLayout.CENTER); // Center-align the button panel

        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void loadComprehensiveReport(JTextArea reportArea) {
        StringBuilder report = new StringBuilder("Comprehensive Report:\n\n");

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Total Customers
            String customerQuery = "SELECT COUNT(*) AS total_customers FROM Customers";
            try (PreparedStatement stmt = conn.prepareStatement(customerQuery);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    report.append("Total Customers: ").append(rs.getInt("total_customers")).append("\n");
                }
            }

            // Total Items
            String itemQuery = "SELECT COUNT(*) AS total_items FROM Items";
            try (PreparedStatement stmt = conn.prepareStatement(itemQuery);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    report.append("Total Items: ").append(rs.getInt("total_items")).append("\n");
                }
            }

            // Total Orders
            String orderQuery = "SELECT COUNT(*) AS total_orders FROM Orders";
            try (PreparedStatement stmt = conn.prepareStatement(orderQuery);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    report.append("Total Orders: ").append(rs.getInt("total_orders")).append("\n");
                }
            }

            reportArea.setText(report.toString());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 123, 255)); // Blue color
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void navigateToSalesReport() {
        new SalesReportScreen(); // Open the Sales Report screen
    }

    private void navigateToInventoryReport() {
        new InventoryReportScreen(); // Open the Inventory Report screen
    }
}