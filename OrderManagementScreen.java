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
    private JTextField nameField, addressField, contactField,ageField;
    private JComboBox<String> preferredTransportBox, deleteOrderBox, searchOrderBox;
    private JRadioButton maleRadioButton, femaleRadioButton;
    private ButtonGroup genderButtonGroup;
    private JPanel mainPanel, genderPanel, southPanel;
    private JLabel nameLabel, addressLabel, genderLabel, ageLabel, contactLabel, transportLabel;
    private JButton addButton, searchButton, deleteButton, backButton;

    public OrderManagementScreen() {
        setTitle("Order Management");
        setSize(1000, 700); // Larger frame size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the frame

        // Main Panel: All Components in One Panel (GridLayout)
        mainPanel = new JPanel(new GridLayout(7, 2, 10, 10)); // Adjusted rows for compactness
        mainPanel.setBackground(new Color(240, 248, 255));

        // Customer Name
        mainPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        mainPanel.add(nameField);

        // Address
        mainPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        mainPanel.add(addressField);

        // Gender
        mainPanel.add(new JLabel("Gender:"));
        genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)); // Compact gender panel
        genderPanel.setBackground(new Color(240, 248, 255));
        maleRadioButton = new JRadioButton("Male");
        femaleRadioButton = new JRadioButton("Female");
        genderButtonGroup = new ButtonGroup();
        genderButtonGroup.add(maleRadioButton);
        genderButtonGroup.add(femaleRadioButton);
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        mainPanel.add(genderPanel);

        mainPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        mainPanel.add(ageField);

        // Contact Details
        mainPanel.add(new JLabel("Contact Details:"));
        contactField = new JTextField();
        mainPanel.add(contactField);

        // Preferred Method of Transport
        mainPanel.add(new JLabel("Preferred Method of Transport:"));
        String[] transportTypes = {"truck", "ship", "air"};
        preferredTransportBox = new JComboBox<>(transportTypes);
        mainPanel.add(preferredTransportBox);

        // Add Customer Button
        addButton = new JButton("Add Customer");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(new Color(0, 123, 255)); // Blue color
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.addActionListener(new ButtonHandler());
        mainPanel.add(addButton);

        // Add Main Panel to Frame
        add(mainPanel, BorderLayout.CENTER);

        // South Panel: Search, Delete, and Back Buttons (GridLayout 3x3)
        southPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        southPanel.setBackground(new Color(240, 248, 255));

        // Search Order Section
        southPanel.add(new JLabel("Search Order:"));
        String[] orderNames = fetchOrderNames();
        searchOrderBox = new JComboBox<>(orderNames);
        searchOrderBox.setSelectedIndex(-1); // No default selection
        southPanel.add(searchOrderBox);
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(0, 123, 255)); // Blue color
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        searchButton.addActionListener(new ButtonHandler());
        southPanel.add(searchButton);

        // Delete Order Section
        southPanel.add(new JLabel("Delete Order:"));
        deleteOrderBox = new JComboBox<>(orderNames);
        deleteOrderBox.setSelectedIndex(-1); // No default selection
        southPanel.add(deleteOrderBox);
        deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(0, 123, 255)); // Blue color
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        deleteButton.addActionListener(new ButtonHandler());
        southPanel.add(deleteButton);

        // Empty Space and Back Button
        southPanel.add(new JLabel()); // Empty space
        southPanel.add(new JLabel()); // Empty space
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(new Color(173, 216, 230)); // Light blue color
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.setPreferredSize(new Dimension(100, 30)); // Smaller button
        backButton.addActionListener(new ButtonHandler());
        southPanel.add(backButton);

        // Add South Panel to Frame
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private class ButtonHandler implements ActionListener{
    	public void actionPerformed (ActionEvent ae) {
    		Object source = ae.getSource();
    		
    		if (source == addButton) {
    			addCustomer();
    		} else if (source == searchButton) {
    			searchOrder();
    		} else if (source == deleteButton) {
    			deleteOrder();
    		} else {
    			dispose();
    		}
    	}
    }


    private void addCustomer() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String contact = contactField.getText().trim();
        String ageText = ageField.getText().trim();

        // Validate inputs
        if (name.isEmpty() || address.isEmpty() || contact.isEmpty() || ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String gender = maleRadioButton.isSelected() ? "Male" : "Female";
        String preferredTransport = (String) preferredTransportBox.getSelectedItem();

        // Insert customer into database
        String query = "INSERT INTO Customers (name, address, gender, age, preferred_transport_type, contact) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, gender);
            stmt.setInt(4, age);
            stmt.setString(5, preferredTransport);
            stmt.setString(6, contact);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Customer added successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
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


    private void navigateToMainDashboard() {
        dispose(); // Close the current screen
    }
}