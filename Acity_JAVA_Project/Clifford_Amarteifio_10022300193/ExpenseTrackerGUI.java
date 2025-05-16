import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.*;

public class ExpenseTrackerGUI extends JFrame {
    private ExpenseManager manager;
    private JTextField amountField;
    private JComboBox<ExpenseCategory> categoryComboBox;
    private JTextField descriptionField;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);  // Blue
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Lighter Blue
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);    // Green
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 243); // Light Gray
    private static final Color TEXT_COLOR = new Color(44, 62, 80);        // Dark Gray
    
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    public ExpenseTrackerGUI() {
        manager = new ExpenseManager();
        
        
        setTitle("Expense Tracker Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        
        
        createExpenseTable();
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        

        updateDisplay();
        
        pack();
        setSize(900, 600);
        setLocationRelativeTo(null);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panel.setBackground(PRIMARY_COLOR);
        
        JLabel titleLabel = new JLabel("Expense Tracker Pro");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Manage your expenses with ease");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(236, 240, 243));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(PRIMARY_COLOR);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        panel.add(textPanel);
        return panel;
    }

    private void createExpenseTable() {
        // Create table model with columns
        String[] columnNames = {"Date", "Amount ($)", "Category", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        expenseTable = new JTable(tableModel);
        expenseTable.setFillsViewportHeight(true);
        expenseTable.setRowHeight(35);
        expenseTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        expenseTable.setGridColor(new Color(180, 180, 180));
        expenseTable.setShowGrid(true);
        expenseTable.setSelectionBackground(new Color(52, 152, 219));
        expenseTable.setSelectionForeground(Color.WHITE);
        expenseTable.setBackground(Color.WHITE);
        
        // Create and set custom header renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                comp.setBackground(new Color(25, 100, 150));
                comp.setForeground(Color.WHITE);
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(20, 80, 120)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                return comp;
            }
        };
        
        // Apply the custom renderer to all column headers
        JTableHeader header = expenseTable.getTableHeader();
        header.setDefaultRenderer(headerRenderer);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        header.setReorderingAllowed(false);  // Prevent column reordering
        
        // Set column widths
        expenseTable.getColumnModel().getColumn(0).setPreferredWidth(120);  // Date
        expenseTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Amount
        expenseTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Category
        expenseTable.getColumnModel().getColumn(3).setPreferredWidth(400);  // Description
        
        // Add selection listener
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configure scroll pane
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        
        Font labelFont = HEADER_FONT;
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel amountLabel = new JLabel("Amount: $");
        amountLabel.setFont(labelFont);
        amountLabel.setForeground(TEXT_COLOR);
        panel.add(amountLabel, gbc);
        
        gbc.gridx = 1;
        amountField = new JTextField(10);
        styleTextField(amountField);
        panel.add(amountField, gbc);
        
        // Category combo box
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(labelFont);
        categoryLabel.setForeground(TEXT_COLOR);
        panel.add(categoryLabel, gbc);
        
        gbc.gridx = 1;
        categoryComboBox = new JComboBox<>(ExpenseCategory.values());
        styleComboBox(categoryComboBox);
        panel.add(categoryComboBox, gbc);
        
        // Description field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(labelFont);
        descLabel.setForeground(TEXT_COLOR);
        panel.add(descLabel, gbc);
        
        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        styleTextField(descriptionField);
        panel.add(descriptionField, gbc);
        
        return panel;
    }
    
    private void styleTextField(JTextField textField) {
        textField.setFont(NORMAL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
    private void styleComboBox(JComboBox<ExpenseCategory> comboBox) {
        comboBox.setFont(NORMAL_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Create buttons with custom styling
        JButton[] buttons = {
            createStyledButton("Add Expense"),
            createStyledButton("Delete Selected"),
            createStyledButton("View Monthly"),
            createStyledButton("View Yearly"),
            createStyledButton("Save to File")
        };
        
        // Add action listeners
        buttons[0].addActionListener(e -> addExpense());
        buttons[1].addActionListener(e -> deleteSelectedExpense());
        buttons[2].addActionListener(e -> showMonthlyExpenses());
        buttons[3].addActionListener(e -> showYearlyExpenses());
        buttons[4].addActionListener(e -> saveToFile());
        
        // Add buttons to panel
        for (JButton button : buttons) {
            panel.add(button);
        }
        
        return panel;
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Expense Report");
        
        // Set default file name with current date
        String defaultFileName = "expense_report_" + LocalDate.now() + ".csv";
        fileChooser.setSelectedFile(new File(defaultFileName));
        
        // Add file filter for CSV files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Ensure file has .csv extension
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                // Write header
                writer.println("Date,Amount,Category,Description");
                
                // Write data
                for (Expense expense : manager.getAllExpenses()) {
                    writer.printf("%s,%.2f,%s,%s%n",
                        expense.getDate(),
                        expense.getAmount(),
                        expense.getCategory(),
                        expense.getDescription());
                }
                
                showSuccess("Expense report saved successfully!");
            } catch (IOException e) {
                showError("Error saving file: " + e.getMessage());
            }
        }
    }

    private void addExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            ExpenseCategory category = (ExpenseCategory) categoryComboBox.getSelectedItem();
            String description = descriptionField.getText();
            
            if (description.trim().isEmpty()) {
                showError("Please enter a description");
                return;
            }
            
            manager.addExpense(amount, category, description);
            
            // Clear input fields
            amountField.setText("");
            descriptionField.setText("");
            
            updateDisplay();
            showSuccess("Expense added successfully!");
        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
        }
    }

    private void deleteSelectedExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an expense to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this expense?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (manager.deleteExpense(selectedRow)) {
                updateDisplay();
                showSuccess("Expense deleted successfully!");
            } else {
                showError("Error deleting expense");
            }
        }
    }

    private void showMonthlyExpenses() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        
        // Clear the table
        tableModel.setRowCount(0);
        
        // Add monthly summary
        Object[] summaryRow = {
            String.format("%d/%d Summary", month, year),
            String.format("$%.2f", manager.getMonthlyExpenses(year, month)),
            "TOTAL",
            ""
        };
        tableModel.addRow(summaryRow);
        
        // Add category breakdown
        Map<ExpenseCategory, Double> expensesByCategory = manager.getMonthlyExpensesByCategory(year, month);
        for (Map.Entry<ExpenseCategory, Double> entry : expensesByCategory.entrySet()) {
            Object[] categoryRow = {
                "",
                String.format("$%.2f", entry.getValue()),
                entry.getKey(),
                "Category Total"
            };
            tableModel.addRow(categoryRow);
        }
    }

    private void showYearlyExpenses() {
        int year = LocalDate.now().getYear();
        
        // Clear the table
        tableModel.setRowCount(0);
        
        // Add yearly summary
        Object[] summaryRow = {
            String.format("Year %d", year),
            String.format("$%.2f", manager.getYearlyExpenses(year)),
            "TOTAL",
            "Yearly Summary"
        };
        tableModel.addRow(summaryRow);
    }

    private void updateDisplay() {
        tableModel.setRowCount(0);
        for (Expense expense : manager.getAllExpenses()) {
            Object[] row = {
                expense.getDate(),
                String.format("$%.2f", expense.getAmount()),
                expense.getCategory(),
                expense.getDescription()
            };
            tableModel.addRow(row);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new ExpenseTrackerGUI().setVisible(true);
        });
    }
} 