package test2;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsScreen extends JFrame {
    private JTextArea reportArea;

    public ReportsScreen() {
        setTitle("Reports");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);

        JButton generateButton = new JButton("Generate Report");
        generateButton.addActionListener(e -> generateReport());

        add(scrollPane, BorderLayout.CENTER);
        add(generateButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void generateReport() {
        String query = "SELECT * FROM Orders";
        StringBuilder report = new StringBuilder("Order Report:\nID\tCustomer\tItem\tQuantity\tStatus\n");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int customerId = rs.getInt("customer_id");
                int itemId = rs.getInt("item_id");
                int quantity = rs.getInt("quantity");
                String status = rs.getString("status");

                report.append(orderId).append("\t")
                      .append(customerId).append("\t")
                      .append(itemId).append("\t")
                      .append(quantity).append("\t")
                      .append(status).append("\n");
            }

            reportArea.setText(report.toString());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}