

import javax.swing.*;
import java.awt.*;

public class PaymentProcessingScreen {
    public PaymentProcessingScreen() {
        JFrame frame = new JFrame("Payment Processing");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("Payment Method:"));
        String[] methods = {"Cash", "Cheque", "Credit Card"};
        JComboBox<String> methodDropdown = new JComboBox<>(methods);
        inputPanel.add(methodDropdown);

        inputPanel.add(new JLabel("Amount:"));
        JTextField amountField = new JTextField();
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Card Number (if applicable):"));
        JTextField cardField = new JTextField();
        inputPanel.add(cardField);

        JButton processButton = new JButton("Process Payment");
        processButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Payment processed successfully."));

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(processButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}