
import javax.swing.*;
import java.awt.*;

public class ReportingScreen {
    public ReportingScreen() {
        JFrame frame = new JFrame("Generate Reports");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setText("Report:\nTotal Orders: 5\nTotal Revenue: $500\nTransport Costs: $50");

        JScrollPane scrollPane = new JScrollPane(reportArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}