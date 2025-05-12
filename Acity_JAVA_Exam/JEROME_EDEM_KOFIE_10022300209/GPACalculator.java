import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class GPACalculator extends JFrame {

    // UI Components
    private final JTextField nameField; // Field for student name
    private final JTextField idField; // Field for student ID
    private JTextField creditHoursField; // Field for credit hours
    private JComboBox<String> gradeCombo; // Dropdown for grades
    private JComboBox<String> storageCombo; // Dropdown for storage options
    private JTextArea resultArea; // Area to display results
    private ArrayList<Course> courses; // List to store courses
    private JButton addCourseBtn, calculateBtn, clearBtn; // Buttons for actions
    private JLabel courseCountLabel; // Label to show number of courses added

    // UI Colors and Fonts
    private Color primaryColor = new Color(51, 153, 255);
    private Color secondaryColor = new Color(240, 240, 240);
    private Font labelFont = new Font("Arial", Font.BOLD, 12);
    
    public GPACalculator() 
        // Set up the main frame
        setTitle("Student GPA Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        
        // Initialize the list of courses
        courses = new ArrayList<>();

        // Create the main panel with vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Welcome Message
        JLabel welcomeLabel = new JLabel("Welcome to GPA Calculator");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(primaryColor);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Student Information Panel
        JPanel studentPanel = createStyledPanel("Student Information");
        addLabelAndField(studentPanel, "Full Name:", nameField = new JTextField());
        addLabelAndField(studentPanel, "Student ID:", idField = new JTextField());
        addLabelAndField(studentPanel, "Storage Type:", storageCombo = new JComboBox<>(
            new String[]{"File", "Database"}));
        
        mainPanel.add(studentPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Course Information Panel
        JPanel coursePanel = createStyledPanel("Course Information");
        addLabelAndField(coursePanel, "Credit Hours:", creditHoursField = new JTextField());
        addLabelAndField(coursePanel, "Grade:", gradeCombo = new JComboBox<>(
            new String[]{"A (4.0)", "B+ (3.5)", "B (3.0)", "C+ (2.5)", 
                        "C (2.0)", "D (1.5)", "E (1.0)", "F (0.0)"}));
        
        // Course count label
        courseCountLabel = new JLabel("Courses added: 0");
        courseCountLabel.setFont(labelFont);
        courseCountLabel.setForeground(primaryColor);
        coursePanel.add(courseCountLabel);
        
        mainPanel.add(coursePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        addCourseBtn = createStyledButton("Add Course", "➕");
        calculateBtn = createStyledButton("Calculate GPA", "📊");
        clearBtn = createStyledButton("Clear All", "🗑️");
        
        buttonPanel.add(addCourseBtn);
        buttonPanel.add(calculateBtn);
        buttonPanel.add(clearBtn);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Results Area
        resultArea = new JTextArea(12, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBackground(secondaryColor);
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
       