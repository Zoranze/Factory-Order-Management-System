
import javax.swing.*;
import java.awt.*;

public class QuoteManagementScreen {
    public QuoteManagementScreen() {
        JFrame frame = new JFrame("Quote Management");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JTextArea quoteArea = new JTextArea();
        quoteArea.setEditable(false);
        quoteArea.setText("Quote 1: Item 1, Quantity 10\nQuote 2: Item 2, Quantity 5");

        JButton convertButton = new JButton("Convert to Order");
        convertButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Quote converted to order."));

        frame.add(new JScrollPane(quoteArea), BorderLayout.CENTER);
        frame.add(convertButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}