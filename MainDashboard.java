
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboard extends JFrame {
    private String role;
    private JPanel headerPanel, centerPanel, mainPanel, logoutPanel;
    private JLabel headerLabel;
    private JButton processOrdersButton, manageOrdersButton, viewCustomersButton, viewItemsButton, viewSalesReportsButton, updateStockLevelsButton, viewInventoryReportsButton, manageItemsButton, viewComprehensiveReportsButton,viewSystemDataButton, searchSystemDataButton, logoutButton; 

    public MainDashboard(String role) {
        this.role = role;
        setTitle("Main Dashboard");
        setSize(1000, 700); // Larger frame size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Header Panel
        headerPanel = new JPanel(new GridLayout(1, 1, 500, 0));
        headerLabel = new JLabel("Main Dashboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.BLACK);
        headerPanel.add(headerLabel);

        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(headerPanel);
        add(centerPanel, BorderLayout.NORTH);

        // Main Panel
        mainPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // Adjusted rows for better spacing
        mainPanel.setBackground(new Color(240, 248, 255));

        // Add navigation buttons based on the user's role
        if ("salesperson".equals(role)) {
            // Process Orders Button
            processOrdersButton = new JButton("Process Orders");
            processOrdersButton.setFont(new Font("Arial", Font.BOLD, 16));
            processOrdersButton.setForeground(Color.WHITE);
            processOrdersButton.setBackground(new Color(0, 123, 255));
            processOrdersButton.setFocusPainted(false);
            processOrdersButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            processOrdersButton.addActionListener(new ButtonHandler());
            mainPanel.add(processOrdersButton);

            // Manage Orders Button
            manageOrdersButton = new JButton("Manage Orders (Add/Delete/Search)");
            manageOrdersButton.setFont(new Font("Arial", Font.BOLD, 16));
            manageOrdersButton.setForeground(Color.WHITE);
            manageOrdersButton.setBackground(new Color(0, 123, 255));
            manageOrdersButton.setFocusPainted(false);
            manageOrdersButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            manageOrdersButton.addActionListener(new ButtonHandler());
            mainPanel.add(manageOrdersButton);

            // View Customers Button
            viewCustomersButton = new JButton("View Customers");
            viewCustomersButton.setFont(new Font("Arial", Font.BOLD, 16));
            viewCustomersButton.setForeground(Color.WHITE);
            viewCustomersButton.setBackground(new Color(0, 123, 255));
            viewCustomersButton.setFocusPainted(false);
            viewCustomersButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            viewCustomersButton.addActionListener(new ButtonHandler());
            mainPanel.add(viewCustomersButton);

            viewItemsButton = new JButton("View Items");
            viewItemsButton.setFont(new Font("Arial", Font.BOLD, 16));
            viewItemsButton.setForeground(Color.WHITE);
            viewItemsButton.setBackground(new Color(0, 123, 255));
            viewItemsButton.setFocusPainted(false);
            viewItemsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            viewItemsButton.addActionListener(new ButtonHandler());
            mainPanel.add(viewItemsButton);
            
            // View Sales Reports Button
            viewSalesReportsButton = new JButton("View Sales Reports");
            viewSalesReportsButton.setFont(new Font("Arial", Font.BOLD, 16));
            viewSalesReportsButton.setForeground(Color.WHITE);
            viewSalesReportsButton.setBackground(new Color(0, 123, 255));
            viewSalesReportsButton.setFocusPainted(false);
            viewSalesReportsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            viewSalesReportsButton.addActionListener(new ButtonHandler());
            mainPanel.add(viewSalesReportsButton);
        } else if ("inventory_officer".equals(role)) {
            // View Items Button
        	viewItemsButton = new JButton ("View Items");
        	viewItemsButton.setFont(new Font("Arial", Font.BOLD, 16));
            viewItemsButton.setForeground(Color.WHITE);
            viewItemsButton.setBackground(new Color(0, 123, 255));
            viewItemsButton.setFocusPainted(false);
            viewItemsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        	viewItemsButton.addActionListener(new ButtonHandler());
            mainPanel.add(viewItemsButton);

            // Update Stock Levels Button
            updateStockLevelsButton = new JButton("Update Stock Levels");
            updateStockLevelsButton.setFont(new Font("Arial", Font.BOLD, 16));
            updateStockLevelsButton.setForeground(Color.WHITE);
            updateStockLevelsButton.setBackground(new Color(0, 123, 255));
            updateStockLevelsButton.setFocusPainted(false);
            updateStockLevelsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            updateStockLevelsButton.addActionListener(new ButtonHandler());
            mainPanel.add(updateStockLevelsButton);

            // View Inventory Reports Button
            viewInventoryReportsButton = new JButton("View Inventory Reports");
            viewInventoryReportsButton.setFont(new Font("Arial", Font.BOLD, 16));
            viewInventoryReportsButton.setForeground(Color.WHITE);
            viewInventoryReportsButton.setBackground(new Color(0, 123, 255));
            viewInventoryReportsButton.setFocusPainted(false);
            viewInventoryReportsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            viewInventoryReportsButton.addActionListener(new ButtonHandler());
            mainPanel.add(viewInventoryReportsButton);
        } else if ("is_manager".equals(role)) {
            // Manage Items Button
            manageItemsButton = new JButton("Manage Item(ADD/DELETE/SEARCH)");
            manageItemsButton.setFont(new Font("Arial", Font.BOLD, 16));
            manageItemsButton.setForeground(Color.WHITE);
            manageItemsButton.setBackground(new Color(0, 123, 255));
            manageItemsButton.setFocusPainted(false);
            manageItemsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            manageItemsButton.addActionListener(new ButtonHandler());
            mainPanel.add(manageItemsButton);

            // View Comprehensive Reports Button
            viewComprehensiveReportsButton = new JButton("View Comprehensive Reports");
            viewComprehensiveReportsButton.setFont(new Font("Arial", Font.BOLD, 16));
            viewComprehensiveReportsButton.setForeground(Color.WHITE);
            viewComprehensiveReportsButton.setBackground(new Color(0, 123, 255));
            viewComprehensiveReportsButton.setFocusPainted(false);
            viewComprehensiveReportsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            viewComprehensiveReportsButton.addActionListener(new ButtonHandler());
            mainPanel.add(viewComprehensiveReportsButton);
            
            viewSystemDataButton = new JButton("View Table");
            viewSystemDataButton.setFont(new Font("Arial", Font.BOLD, 16));
            viewSystemDataButton.setForeground(Color.WHITE);
            viewSystemDataButton.setBackground(new Color(0, 123, 255));
            viewSystemDataButton.setFocusPainted(false);
            viewSystemDataButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            viewSystemDataButton.addActionListener(new ButtonHandler());
            mainPanel.add(viewSystemDataButton);
            

            // Search System Data Button
            searchSystemDataButton = new JButton("Search System Data");
            searchSystemDataButton.setFont(new Font("Arial", Font.BOLD, 16));
            searchSystemDataButton.setForeground(Color.WHITE);
            searchSystemDataButton.setBackground(new Color(0, 123, 255));
            searchSystemDataButton.setFocusPainted(false);
            searchSystemDataButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            searchSystemDataButton.addActionListener(new ButtonHandler());
            mainPanel.add(searchSystemDataButton);
                 
        }

        add(mainPanel, BorderLayout.CENTER);

        // Logout Button Panel
        logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        logoutPanel.setBackground(new Color(240, 248, 255));

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.RED);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutButton.addActionListener(new ButtonHandler());
        logoutPanel.add(logoutButton);

        add(logoutPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public class ButtonHandler implements ActionListener {
    	public void actionPerformed(ActionEvent ae) {
    		Object source = ae.getSource();
    		
    		if (source == processOrdersButton) {
    			new OrderProcessingScreen();
    		} else if (source == manageOrdersButton) {
    			new OrderManagementScreen();
    		} else if (source == viewCustomersButton) {
    			new CustomerViewScreen();
    		} else if (source == viewItemsButton) {
    			new ItemViewScreen();
    		} else if (source == viewSalesReportsButton) {
    			new SalesReportScreen();
    		} else if (source == updateStockLevelsButton ) {
    			new StockManagementScreen();
    		} else if (source == viewInventoryReportsButton) {
    			new InventoryReportScreen();
    		} else if (source == manageItemsButton) {
    			new ItemManagementScreen();
    		} else if (source ==  viewComprehensiveReportsButton) {
    			new ComprehensiveReportScreen();
    		} else if (source == searchSystemDataButton) {
    			new UniversalSearchScreen();
    		} else if (source == viewSystemDataButton) {
    			new OrdersViewScreen();
    		} else {
    			logout();
    		}
    	}
    }
    
    
    public void logout () {
    	int confirm = JOptionPane.showConfirmDialog(this,"Are you sure you want to logout?","Logout Confirmation",JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // Close the current dashboard
                new LoginScreen(); // Redirect to the login screen (assuming you have a LoginScreen class)
            }
    }
    
}