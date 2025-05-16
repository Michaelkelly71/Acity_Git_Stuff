import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.*;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField, courseField, creditHoursField;
    private JComboBox<String> gradeCombo, storageCombo;
    private JButton addButton, calculateButton;
    private JTextArea resultArea;
    private ArrayList<CourseInfo> courses;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gpa_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        courses = new ArrayList<>();

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
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
        String[] grades = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F"};
        gradeCombo = new JComboBox<>(grades);
        inputPanel.add(gradeCombo);

        inputPanel.add(new JLabel("Storage Type:"));
        String[] storageTypes = {"File", "Database"};
        storageCombo = new JComboBox<>(storageTypes);
        inputPanel.add(storageCombo);

        // Buttons
        addButton = new JButton("Add Course");
        calculateButton = new JButton("Calculate GPA");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(calculateButton);
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
                    int credits = Integer.parseInt(creditHoursField.getText());
                    String grade = (String) gradeCombo.getSelectedItem();
                    
                    courses.add(new CourseInfo(course, credits, grade));
                    
                    courseField.setText("");
                    creditHoursField.setText("");
                    resultArea.append(String.format("Added: %s, %d credits, Grade: %s\n", 
                        course, credits, grade));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid credit hours!");
                }
            }
        });

        // Calculate GPA Button Action
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (courses.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please add at least one course!");
                    return;
                }

                double totalPoints = 0;
                int totalCredits = 0;

                for (CourseInfo course : courses) {
                    totalPoints += course.getCredits() * getGradePoints(course.getGrade());
                    totalCredits += course.getCredits();
                }

                double gpa = totalPoints / totalCredits;
                String storageType = (String) storageCombo.getSelectedItem();
                
                if (storageType.equals("Database")) {
                    saveToDatabase(nameField.getText(), idField.getText(), gpa);
                }
                
                String result = String.format("\n----- GPA Calculation Result -----\n" +
                    "Student Name: %s\n" +
                    "Student ID: %s\n" +
                    "Storage Type: %s\n" +
                    "GPA: %.2f\n", 
                    nameField.getText(), idField.getText(), storageType, gpa);
                
                resultArea.append(result);
            }
        });
    }

    private void saveToDatabase(String name, String id, double gpa) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO students (student_name, student_id, gpa) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, id);
            pstmt.setDouble(3, gpa);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Results saved to database successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error saving to database: " + ex.getMessage());
        }
    }

    private double getGradePoints(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
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
