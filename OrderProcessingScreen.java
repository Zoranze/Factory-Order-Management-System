
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OrderProcessingScreen {
    private JFrame frame;
    private JComboBox<String> itemDropdown;
    private JTextField quantityField;
    private JTextArea orderListArea;
    private List<Order> orders;

    public OrderProcessingScreen(String role) {
        frame = new JFrame("Order Processing System - " + role);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        orders = new ArrayList<>();

        // Panel for Input Fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Item Selection Dropdown
        inputPanel.add(new JLabel("Select Item:"));
        String[] items = {"Item 1", "Item 2", "Item 3"};
        itemDropdown = new JComboBox<>(items);
        inputPanel.add(itemDropdown);

        // Quantity Input Field
        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        // Add Order Button
        JButton addButton = new JButton("Add Order");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String item = (String) itemDropdown.getSelectedItem();
                String quantityText = quantityField.getText();

                if (quantityText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int quantity = Integer.parseInt(quantityText);
                Order order = new Order(item, quantity);
                orders.add(order);
                updateOrderList();
                clearFields();
            }
        });
        inputPanel.add(addButton);

        // Delete Order Button
        JButton deleteButton = new JButton("Delete Order");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the last order?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (selectedIndex == JOptionPane.YES_OPTION && !orders.isEmpty()) {
                    orders.remove(orders.size() - 1);
                    updateOrderList();
                }
            }
        });
        inputPanel.add(deleteButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Order List Display
        orderListArea = new JTextArea();
        orderListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderListArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    

	private void updateOrderList() {
        StringBuilder orderList = new StringBuilder("Order List:\n");
        for (Order order : orders) {
            orderList.append("Item: ").append(order.getItem()).append(", Quantity: ").append(order.getQuantity()).append("\n");
        }
        orderListArea.setText(orderList.toString());
    }

    private void clearFields() {
        quantityField.setText("");
    }
}

// Simple Order Class
class Order {
    private String item;
    private int quantity;

    public Order(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
}