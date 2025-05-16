import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GpaCalculator extends JFrame {
    private JTextField nameField, idField, courseField, creditField;
    private JComboBox<String> gradeBox, saveOptionBox;
    private JTextArea courseListArea;
    private JButton addCourseBtn, calcGpaBtn, saveBtn;
    private JLabel gpaLabel, messageLabel;

    private java.util.List<Course> courses = new ArrayList<>();
    private double calculatedGpa = -1.0;

    private static final Map<String, Double> gradePointMap = new HashMap<>();
    static {
        gradePointMap.put("A", 4.0);
        gradePointMap.put("B+", 3.5);
        gradePointMap.put("B", 3.0);
        gradePointMap.put("C+", 2.5);
        gradePointMap.put("C", 2.0);
        gradePointMap.put("D", 1.5);
        gradePointMap.put("E", 1.0);
        gradePointMap.put("F", 0.0);
    }

    public GpaCalculator() {
        setTitle("GPA Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel for Student Info
        JPanel topPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Student Info"));
        nameField = new JTextField();
        idField = new JTextField();
        courseField = new JTextField();
        creditField = new JTextField();
        gradeBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
        addCourseBtn = new JButton("Add Course");

        topPanel.add(new JLabel("Name:"));
        topPanel.add(nameField);
        topPanel.add(new JLabel("ID:"));
        topPanel.add(idField);

        topPanel.add(new JLabel("Course:"));
        topPanel.add(courseField);
        topPanel.add(new JLabel("Credit Hours:"));
        topPanel.add(creditField);

        topPanel.add(new JLabel("Grade:"));
        topPanel.add(gradeBox);
        topPanel.add(addCourseBtn);
        topPanel.add(new JLabel("Save Option:"));
        saveOptionBox = new JComboBox<>(new String[]{"File", "Database"});
        topPanel.add(saveOptionBox);

        // Middle Panel for Course List
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBorder(BorderFactory.createTitledBorder("Courses Added"));
        courseListArea = new JTextArea(8, 40);
        courseListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(courseListArea);
        middlePanel.add(scrollPane, BorderLayout.CENTER);

        // Action Panel for GPA and Save
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        calcGpaBtn = new JButton("Calculate GPA");
        gpaLabel = new JLabel("GPA: ");
        saveBtn = new JButton("Save Data");
        actionPanel.add(calcGpaBtn);
        actionPanel.add(gpaLabel);
        actionPanel.add(saveBtn);

        // Message Label
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);

        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        add(messageLabel, BorderLayout.PAGE_END);

        // Action Listeners
        addCourseBtn.addActionListener(e -> addCourse());
        calcGpaBtn.addActionListener(e -> calculateGpa());
        saveBtn.addActionListener(e -> saveData());
    }

    private void addCourse() {
        String course = courseField.getText().trim();
        String creditStr = creditField.getText().trim();
        String grade = (String) gradeBox.getSelectedItem();
        if (course.isEmpty() || creditStr.isEmpty()) {
            messageLabel.setText("Course and Credit Hours are required.");
            return;
        }
        int credit;
        try {
            credit = Integer.parseInt(creditStr);
            if (credit <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            messageLabel.setText("Credit Hours must be a positive integer.");
            return;
        }
        courses.add(new Course(course, credit, grade));
        courseListArea.append(course + " | " + credit + " | " + grade + "\\n");
        courseField.setText("");
        creditField.setText("");
        messageLabel.setText("");
        calculatedGpa = -1.0; // Reset GPA when new course is added
        gpaLabel.setText("GPA: ");
    }

    private void calculateGpa() {
        if (courses.isEmpty()) {
            messageLabel.setText("Add at least one course.");
            return;
        }
        double totalPoints = 0;
        int totalCredits = 0;
        for (Course c : courses) {
            double gp = gradePointMap.getOrDefault(c.grade, 0.0);
            totalPoints += gp * c.credit;
            totalCredits += c.credit;
        }
        if (totalCredits == 0) {
            messageLabel.setText("Total credit hours cannot be zero.");
            return;
        }
        calculatedGpa = totalPoints / totalCredits;
        gpaLabel.setText(String.format("GPA: %.2f", calculatedGpa));
        messageLabel.setText("");
    }

    private void saveData() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String saveOption = (String) saveOptionBox.getSelectedItem();
        if (name.isEmpty() || id.isEmpty()) {
            messageLabel.setText("Name and ID are required.");
            return;
        }
        if (courses.isEmpty()) {
            messageLabel.setText("Add at least one course.");
            return;
        }
        if (calculatedGpa < 0) {
            messageLabel.setText("Please calculate GPA before saving.");
            return;
        }
        if (saveOption.equals("File")) {
            try (PrintWriter out = new PrintWriter(new FileWriter("gpa_records.txt", true))) {
                out.println("Name: " + name);
                out.println("ID: " + id);
                out.println("Courses:");
                for (Course c : courses) {
                    out.println("  " + c.course + " | " + c.credit + " | " + c.grade);
                }
                out.printf("GPA: %.2f\\n", calculatedGpa);
                out.println("--------------------------");
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Data saved to gpa_records.txt");
            } catch (IOException ex) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Error saving to file.");
            }
        } else {
            messageLabel.setForeground(Color.BLUE);
            messageLabel.setText("Database: Under Construction.");
        }
    }

    static class Course {
        String course;
        int credit;
        String grade;
        Course(String course, int credit, String grade) {
            this.course = course;
            this.credit = credit;
            this.grade = grade;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GpaCalculator().setVisible(true));
    }
}