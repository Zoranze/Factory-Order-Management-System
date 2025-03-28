package test2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class OrderManagementScreen extends JFrame {
    private JTextField nameField, addressField, contactField;
    private JComboBox<String> preferredTransportBox;
    private JRadioButton maleRadioButton, femaleRadioButton;
    private ButtonGroup genderButtonGroup;
    private JComboBox<String> deleteOrderBox, searchOrderBox;

    public OrderManagementScreen() {
        setTitle("Order Management");
        setSize(1000, 700); // Larger frame size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background
        setLocationRelativeTo(null); // Center the frame

        // North Panel: Customer Details (GridLayout 5x2)
        JPanel northPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        northPanel.setBackground(new Color(240, 248, 255));

        northPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        northPanel.add(nameField);

        northPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        northPanel.add(addressField);

        northPanel.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(new Color(240, 248, 255));
        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        genderButtonGroup = new ButtonGroup();
        genderButtonGroup.add(maleRadioButton);
        genderButtonGroup.add(femaleRadioButton);
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        northPanel.add(genderPanel);

        northPanel.add(new JLabel("Contact Details:"));
        contactField = new JTextField();
        northPanel.add(contactField);

        northPanel.add(new JLabel("Preferred Method of Transport:"));
        String[] transportTypes = {"truck", "ship", "air"};
        preferredTransportBox = new JComboBox<>(transportTypes);
        northPanel.add(preferredTransportBox);

        add(northPanel, BorderLayout.NORTH);

        // Center Panel: Add Customer Button (GridLayout 1x1)
        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        centerPanel.setBackground(new Color(240, 248, 255));

        JButton addButton = new JButton("Add Customer");
        styleButton(addButton);
        addButton.addActionListener(e -> addCustomer());
        centerPanel.add(addButton);

        add(centerPanel, BorderLayout.CENTER);

        // South Panel: Search, Delete, and Back Buttons (GridLayout 3x3)
        JPanel southPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        southPanel.setBackground(new Color(240, 248, 255));

        // Search Order Section
        southPanel.add(new JLabel("Search Order:"));
        String[] orderNames = fetchOrderNames();
        searchOrderBox = new JComboBox<>(orderNames);
        searchOrderBox.setSelectedIndex(-1); // No default selection
        southPanel.add(searchOrderBox);

        JButton searchButton = new JButton("Search");
        styleButton(searchButton);
        searchButton.addActionListener(e -> searchOrder());
        southPanel.add(searchButton);

        // Delete Order Section
        southPanel.add(new JLabel("Delete Order:"));
        deleteOrderBox = new JComboBox<>(orderNames);
        deleteOrderBox.setSelectedIndex(-1); // No default selection
        southPanel.add(deleteOrderBox);

        JButton deleteButton = new JButton("Delete");
        styleButton(deleteButton);
        deleteButton.addActionListener(e -> deleteOrder());
        southPanel.add(deleteButton);

        // Empty Space and Back Button
        southPanel.add(new JLabel()); // Empty space
        southPanel.add(new JLabel()); // Empty space

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(new Color(173, 216, 230)); // Light blue color
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.setPreferredSize(new Dimension(100, 30)); // Smaller button
        backButton.addActionListener(e -> navigateToMainDashboard());
        southPanel.add(backButton);

        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addCustomer() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String contact = contactField.getText().trim();

        // Validate inputs
        if (name.isEmpty() || address.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String gender = maleRadioButton.isSelected() ? "Male" : "Female";
        String preferredTransport = (String) preferredTransportBox.getSelectedItem();

        // Insert customer into database
        String query = "INSERT INTO Customers (name, address, gender, preferred_transport_type, contact_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, gender);
            stmt.setString(4, preferredTransport);
            stmt.setString(5, contact);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Customer added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteOrder() {
        String selectedOrder = (String) deleteOrderBox.getSelectedItem();
        if (selectedOrder == null || selectedOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an order to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int orderId = getOrderIdByName(selectedOrder);

        String query = "DELETE FROM Orders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Order deleted successfully!");
                refreshOrderDropdowns(); // Refresh dropdowns after deletion
            } else {
                JOptionPane.showMessageDialog(this, "Order not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchOrder() {
        String selectedOrder = (String) searchOrderBox.getSelectedItem();
        if (selectedOrder == null || selectedOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an order to search!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int orderId = getOrderIdByName(selectedOrder);

        String query = "SELECT * FROM Orders o " +
                       "JOIN Customers c ON o.customer_id = c.customer_id " +
                       "WHERE o.order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            StringBuilder result = new StringBuilder("Order Details:\n");
            if (rs.next()) {
                result.append("Order ID: ").append(rs.getInt("order_id"))
                      .append(", Customer: ").append(rs.getString("name"))
                      .append(", Status: ").append(rs.getString("status"))
                      .append("\n");
            }

            if (result.toString().equals("Order Details:\n")) {
                JOptionPane.showMessageDialog(this, "Order not found!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, result.toString(), "Order Details", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String[] fetchOrderNames() {
        String query = "SELECT CONCAT('Order ID: ', order_id, ' - Customer: ', c.name) AS order_name " +
                       "FROM Orders o JOIN Customers c ON o.customer_id = c.customer_id";
        List<String> orderNamesList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                orderNamesList.add(rs.getString("order_name"));
            }
            return orderNamesList.toArray(new String[0]);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching orders: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return new String[0]; // Return an empty array in case of error
        }
    }

    private int getOrderIdByName(String orderName) {
        String query = "SELECT order_id FROM Orders WHERE CONCAT('Order ID: ', order_id, ' - Customer: ', (SELECT name FROM Customers WHERE customer_id = Orders.customer_id)) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, orderName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("order_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // Invalid order ID
    }

    private void refreshOrderDropdowns() {
        String[] orderNames = fetchOrderNames();
        deleteOrderBox.setModel(new DefaultComboBoxModel<>(orderNames));
        searchOrderBox.setModel(new DefaultComboBoxModel<>(orderNames));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 123, 255)); // Blue color
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void navigateToMainDashboard() {
        dispose(); // Close the current screen
       
    }
}