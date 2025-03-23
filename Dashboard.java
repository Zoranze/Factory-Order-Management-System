package ORDER;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard {
    private JFrame frame;
    private String role;

    public Dashboard(String role) {
        this.role = role;

        frame = new JFrame("Dashboard - " + role);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#4CAF50"));
        JLabel headerLabel = new JLabel("Welcome, " + role, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton orderButton = createStyledButton("Order Processing");
        orderButton.addActionListener(e -> new OrderProcessingScreen( role));

        JButton inventoryButton = createStyledButton("Inventory Management");
        inventoryButton.addActionListener(e -> new InventoryManagementScreen());

        JButton quoteButton = createStyledButton("Quote Management");
        quoteButton.addActionListener(e -> new QuoteManagementScreen());

        JButton paymentButton = createStyledButton("Payment Processing");
        paymentButton.addActionListener(e -> new PaymentProcessingScreen());

        JButton reportButton = createStyledButton("Generate Reports");
        reportButton.addActionListener(e -> new ReportingScreen());

        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginScreen();
        });

        buttonPanel.add(orderButton);
        buttonPanel.add(inventoryButton);
        buttonPanel.add(quoteButton);
        buttonPanel.add(paymentButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(logoutButton);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(Color.decode("#4CAF50"));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
}