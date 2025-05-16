import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.*;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField, courseField, creditHoursField;
    private JComboBox<String> gradeCombo, storageCombo;
    private JButton addButton, calculateButton, clearButton;
    private JTextArea resultArea;
    private JLabel currentGPALabel;
    private ArrayList<CourseInfo> courses;
    
    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        courses = new ArrayList<>();

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Course Name:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Credit Hours:"));
        creditHoursField = new JTextField();
        inputPanel.add(creditHoursField);

        inputPanel.add(new JLabel("Grade:"));
        String[] grades = {"A", "B+", "B", "C+", "C", "D+", "E", "F"};
        gradeCombo = new JComboBox<>(grades);
        inputPanel.add(gradeCombo);

        inputPanel.add(new JLabel("Storage Type:"));
        String[] storageTypes = {"File", "Database"};
        storageCombo = new JComboBox<>(storageTypes);
        inputPanel.add(storageCombo);

        // Current GPA Display
        inputPanel.add(new JLabel("Current GPA:"));
        currentGPALabel = new JLabel("0.00");
        currentGPALabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(currentGPALabel);

        // Buttons
        addButton = new JButton("Add Course");
        calculateButton = new JButton("Calculate & Save");
        clearButton = new JButton("Clear All");
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        inputPanel.add(new JLabel()); // Empty label for grid alignment
        inputPanel.add(buttonPanel);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add Course Button Action
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String course = courseField.getText();
                    if (course.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter a course name!");
                        return;
                    }
                    int credits = Integer.parseInt(creditHoursField.getText());
                    String grade = (String) gradeCombo.getSelectedItem();
                    
                    courses.add(new CourseInfo(course, credits, grade));
                    
                    courseField.setText("");
                    creditHoursField.setText("");
                    resultArea.append(String.format("Added: %s, %d credits, Grade: %s\n", 
                        course, credits, grade));
                    
                    // Calculate and display current GPA after each addition
                    updateCurrentGPA();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid credit hours!");
                }
            }
        });

        // Calculate & Save Button Action
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().isEmpty() || idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter student name and ID!");
                    return;
                }
                
                if (courses.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please add at least one course!");
                    return;
                }

                double gpa = calculateGPA();
                String storageType = (String) storageCombo.getSelectedItem();
                
                if (storageType.equals("Database")) {
                    saveToDatabase(nameField.getText(), idField.getText(), gpa);
                } else {
                    saveToFile(nameField.getText(), idField.getText(), gpa);
                }
                
                String result = String.format("\n----- Final GPA Calculation -----\n" +
                    "Student Name: %s\n" +
                    "Student ID: %s\n" +
                    "Storage Type: %s\n" +
                    "Final GPA: %.2f\n", 
                    nameField.getText(), idField.getText(), storageType, gpa);
                
                resultArea.append(result);
            }
        });

        // Clear Button Action
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                courses.clear();
                nameField.setText("");
                idField.setText("");
                courseField.setText("");
                creditHoursField.setText("");
                resultArea.setText("");
                currentGPALabel.setText("0.00");
                JOptionPane.showMessageDialog(null, "All data cleared!");
            }
        });
    }

    private void updateCurrentGPA() {
        if (courses.isEmpty()) {
            currentGPALabel.setText("0.00");
            return;
        }
        
        double gpa = calculateGPA();
        currentGPALabel.setText(String.format("%.2f", gpa));
    }

    private double calculateGPA() {
        double totalPoints = 0;
        int totalCredits = 0;

        for (CourseInfo course : courses) {
            totalPoints += course.getCredits() * getGradePoints(course.getGrade());
            totalCredits += course.getCredits();
        }

        return totalCredits == 0 ? 0 : totalPoints / totalCredits;
    }

    private void saveToDatabase(String name, String id, double gpa) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gpa_calculator", "root", "alisha@123")) {
            String sql = "INSERT INTO students (student_name, student_id, gpa) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, id);
            pstmt.setDouble(3, gpa);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Results saved to database successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Under construction");
        }
    }

    private void saveToFile(String name, String id, double gpa) {
        try {
            String result = String.format("Student Name: %s\nStudent ID: %s\nGPA: %.2f\n\n", 
                name, id, gpa);
            java.io.FileWriter fw = new java.io.FileWriter("gpa_records.txt", true);
            fw.write(result);
            fw.close();
            JOptionPane.showMessageDialog(null, "Results saved to file successfully!");
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving to file: " + ex.getMessage());
        }
    }

    private double getGradePoints(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D+": return 1.5;
            case "E": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    private class CourseInfo {
        private String courseName;
        private int credits;
        private String grade;

        public CourseInfo(String courseName, int credits, String grade) {
            this.courseName = courseName;
            this.credits = credits;
            this.grade = grade;
        }

        public int getCredits() { return credits; }
        public String getGrade() { return grade; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GPACalculator().setVisible(true);
            }
        });
    }
}