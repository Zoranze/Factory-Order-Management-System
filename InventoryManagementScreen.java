
import javax.swing.*;
import java.awt.*;

public class InventoryManagementScreen {
    public InventoryManagementScreen() {
        JFrame frame = new JFrame("Inventory Management");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JTextArea inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        inventoryArea.setText("Item 1: 50 units\nItem 2: 20 units\nItem 3: 0 units (Out of Stock)");

        JScrollPane scrollPane = new JScrollPane(inventoryArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}