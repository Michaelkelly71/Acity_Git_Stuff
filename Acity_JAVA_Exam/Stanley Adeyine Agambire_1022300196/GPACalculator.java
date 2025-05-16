import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JButton addBtn, calculateBtn;
    private JPanel coursesPanel;
    private ArrayList<Course> courses = new ArrayList<>();
    private HashMap<String, Double> gradeValues = new HashMap<>();
    private JComboBox<String> saveOptionBox;

    public GPACalculator() {

        gradeValues.put("A", 4.0);
        gradeValues.put("B+", 3.5);
        gradeValues.put("B", 3.0);
        gradeValues.put("C+", 2.5);
        gradeValues.put("C", 2.0);
        gradeValues.put("D", 1.5);
        gradeValues.put("E", 1.0);
        gradeValues.put("F", 0.0);

        setTitle("GPA Calculator");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        


        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        infoPanel.add(nameField);
        infoPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        infoPanel.add(idField);
        infoPanel.add(new JLabel("Save Option:"));
        saveOptionBox = new JComboBox<>(new String[]{"File", "Database"});
        infoPanel.add(saveOptionBox);

    
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(coursesPanel);

        
        JPanel buttonPanel = new JPanel();
        addBtn = new JButton("Add Course");
        addBtn.addActionListener(e -> addCourse());
        calculateBtn = new JButton("Calculate & Save");
        calculateBtn.addActionListener(e -> calculateAndSave());
        buttonPanel.add(addBtn);
        buttonPanel.add(calculateBtn);

        
        add(infoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addCourse(); 
    }

    private void addCourse() {
        CoursePanel coursePanel = new CoursePanel();
        coursesPanel.add(coursePanel);
        coursesPanel.revalidate();
    }

    private void calculateAndSave() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String saveOption = (String) saveOptionBox.getSelectedItem();

        if (name.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter student name and ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        courses.clear();
        double totalPoints = 0;
        double totalCredits = 0;
        StringBuilder record = new StringBuilder();

        record.append("=== STUDENT GPA SUMMARY ===\n");
        record.append("Name: ").append(name).append("\n");
        record.append("ID: ").append(id).append("\n\n");
        record.append("COURSES:\n");

        for (Component comp : coursesPanel.getComponents()) {
            if (comp instanceof CoursePanel) {
                CoursePanel cp = (CoursePanel) comp;
                try {
                    String courseName = cp.getCourseName();
                    double credits = cp.getCredits();
                    String grade = cp.getGrade();

                    if (courseName.isEmpty()) throw new IllegalArgumentException("Course name is empty");
                    Double gradeValue = gradeValues.get(grade);
                    if (gradeValue == null) throw new IllegalArgumentException("Invalid grade");

                    totalPoints += gradeValue * credits;
                    totalCredits += credits;

                    courses.add(new Course(courseName, credits, grade));
                    record.append(String.format("- %s: Grade %s, %.1f Credits\n", courseName, grade, credits));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter at least one course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        double gpa = totalPoints / totalCredits;
        DecimalFormat df = new DecimalFormat("0.00");
        String gpaString = df.format(gpa);
        record.append("\nCALCULATED GPA: ").append(gpaString);

        
        JOptionPane.showMessageDialog(this, record.toString(), "GPA Result", JOptionPane.INFORMATION_MESSAGE);

        
        if ("File".equals(saveOption)) {
            saveToFile(record.toString());
        } else if ("Database".equals(saveOption)) {
            JOptionPane.showMessageDialog(this, "Database: Under Construction", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveToFile(String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("gpa_records.txt", true))) {
            writer.println(content);
            writer.println("Saved On: " + new Date());
            writer.println("========================================");
            JOptionPane.showMessageDialog(this, "Saved to file successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GPACalculator().setVisible(true));
    }

    // Course class
    private class Course {
        String name;
        double credits;
        String grade;

        public Course(String name, double credits, String grade) {
            this.name = name;
            this.credits = credits;
            this.grade = grade;
        }
    }

    private class CoursePanel extends JPanel {
        private JTextField nameField, creditField;
        private JComboBox<String> gradeCombo;
        private JButton removeBtn;

        public CoursePanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setMaximumSize(new Dimension(Short.MAX_VALUE, 35));

            nameField = new JTextField(12);
            creditField = new JTextField(5);
            gradeCombo = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});
            removeBtn = new JButton("Remove");

            removeBtn.addActionListener(e -> {
                coursesPanel.remove(this);
                coursesPanel.revalidate();
                coursesPanel.repaint();
            });

            add(new JLabel("Course:"));
            add(nameField);
            add(new JLabel("Credits:"));
            add(creditField);
            add(new JLabel("Grade:"));
            add(gradeCombo);
            add(removeBtn);
        }

        public String getCourseName() {
            return nameField.getText().trim();
        }

        public double getCredits() throws NumberFormatException {
            return Double.parseDouble(creditField.getText().trim());
        }

        public String getGrade() {
            return (String) gradeCombo.getSelectedItem();
        }
    }
}
