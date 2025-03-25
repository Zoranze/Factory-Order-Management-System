package test2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderProcessingScreen extends JFrame {
    private JTextField customerIdField, itemIdField, quantityField;

    public OrderProcessingScreen() {
        setTitle("Order Processing");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Customer ID:"));
        customerIdField = new JTextField();
        add(customerIdField);

        add(new JLabel("Item ID:"));
        itemIdField = new JTextField();
        add(itemIdField);

        add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        add(quantityField);

        JButton processButton = new JButton("Process Order");
        processButton.addActionListener(e -> processOrder());
        add(processButton);

        setVisible(true);
    }

    private void processOrder() {
        int customerId = Integer.parseInt(customerIdField.getText());
        int itemId = Integer.parseInt(itemIdField.getText());
        int requestedQuantity = Integer.parseInt(quantityField.getText());

        String checkStockSQL = "SELECT stock_level, cost_price FROM Items WHERE item_id = ?";
        String updateStockSQL = "UPDATE Items SET stock_level = ? WHERE item_id = ?";
        String addOrderSQL = "INSERT INTO Orders (customer_id, item_id, quantity, status, transport_charge) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check stock level and cost price
            try (PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSQL)) {
                checkStockStmt.setInt(1, itemId);
                ResultSet rs = checkStockStmt.executeQuery();

                if (rs.next()) {
                    int stockLevel = rs.getInt("stock_level");
                    double costPrice = rs.getDouble("cost_price");

                    if (stockLevel >= requestedQuantity) {
                        // Sufficient stock: Update stock and create order
                        try (PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL);
                             PreparedStatement addOrderStmt = conn.prepareStatement(addOrderSQL)) {

                            updateStockStmt.setInt(1, stockLevel - requestedQuantity);
                            updateStockStmt.setInt(2, itemId);
                            updateStockStmt.executeUpdate();

                            double transportCharge = calculateTransportCharge(customerId);
                            double totalCost = requestedQuantity * costPrice + transportCharge;

                            addOrderStmt.setInt(1, customerId);
                            addOrderStmt.setInt(2, itemId);
                            addOrderStmt.setInt(3, requestedQuantity);
                            addOrderStmt.setString(4, "shipped");
                            addOrderStmt.setDouble(5, transportCharge);
                            addOrderStmt.executeUpdate();

                            // Display bill notification
                            JOptionPane.showMessageDialog(null, 
                                "Order processed successfully!\n" +
                                "Bill Details:\n" +
                                "Item Cost: $" + (requestedQuantity * costPrice) + "\n" +
                                "Transport Charge: $" + transportCharge + "\n" +
                                "Total Bill: $" + totalCost
                            );
                        }
                    } else {
                        // Insufficient stock: Create backorder
                        int availableQuantity = stockLevel;
                        int backorderedQuantity = requestedQuantity - availableQuantity;

                        try (PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL);
                             PreparedStatement addOrderStmt = conn.prepareStatement(addOrderSQL)) {

                            // Ship available items
                            updateStockStmt.setInt(1, 0); // Set stock to 0
                            updateStockStmt.setInt(2, itemId);
                            updateStockStmt.executeUpdate();

                            double transportCharge = calculateTransportCharge(customerId);
                            double shippedCost = availableQuantity * costPrice + transportCharge;

                            addOrderStmt.setInt(1, customerId);
                            addOrderStmt.setInt(2, itemId);
                            addOrderStmt.setInt(3, availableQuantity);
                            addOrderStmt.setString(4, "shipped");
                            addOrderStmt.setDouble(5, transportCharge);
                            addOrderStmt.executeUpdate();

                            // Create backorder
                            addOrderStmt.setInt(3, backorderedQuantity);
                            addOrderStmt.setString(4, "backordered");
                            addOrderStmt.setDouble(5, 0); // No transport charge for backorders
                            addOrderStmt.executeUpdate();

                            // Display bill notification
                            JOptionPane.showMessageDialog(null, 
                                "Backorder created for missing items.\n" +
                                "Bill Details for Shipped Items:\n" +
                                "Item Cost: $" + (availableQuantity * costPrice) + "\n" +
                                "Transport Charge: $" + transportCharge + "\n" +
                                "Total Bill: $" + shippedCost
                            );
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calculateTransportCharge(int customerId) {
        String query = "SELECT default_transport_type FROM Customers WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String transportType = rs.getString("default_transport_type");
                return getTransportCharge(transportType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Default transport charge
    }

    private double getTransportCharge(String transportType) {
        String query = "SELECT charge FROM TransportCharges WHERE transport_type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, transportType);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("charge");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Default transport charge
    }
}