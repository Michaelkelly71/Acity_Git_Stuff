import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JComboBox<String> storageComboBox;
    private JButton addCourseBtn, calculateBtn;
    private JPanel coursesPanel;
    private List<JTextField> courseFields = new ArrayList<>();
    private List<JComboBox<String>> creditComboBoxes = new ArrayList<>();
    private List<JComboBox<String>> gradeComboBoxes = new ArrayList<>();
    
    private static final Map<String, Double> GRADE_POINTS = new HashMap<>();
    static {
        GRADE_POINTS.put("A", 4.0);
        GRADE_POINTS.put("B+", 3.5);
        GRADE_POINTS.put("B", 3.0);
        GRADE_POINTS.put("C+", 2.5);
        GRADE_POINTS.put("C", 2.0);
        GRADE_POINTS.put("D", 1.5);
        GRADE_POINTS.put("E", 1.0);
        GRADE_POINTS.put("F", 0.0);
    }

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        JPanel topPanel = new JPanel(new GridLayout(3, 2));
        topPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        topPanel.add(nameField);
        topPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        topPanel.add(idField);
        topPanel.add(new JLabel("Save to:"));
        storageComboBox = new JComboBox<>(new String[]{"File", "Database"});
        topPanel.add(storageComboBox);
        add(topPanel, BorderLayout.NORTH);

        
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        addCourse(); // Add one course by default
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        add(scrollPane, BorderLayout.CENTER);

        
        JPanel bottomPanel = new JPanel();
        addCourseBtn = new JButton("Add Course");
        addCourseBtn.addActionListener(e -> addCourse());
        bottomPanel.add(addCourseBtn);
        
        calculateBtn = new JButton("Calculate GPA");
        calculateBtn.addActionListener(new CalculateListener());
        bottomPanel.add(calculateBtn);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addCourse() {
        JPanel coursePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JTextField courseField = new JTextField(15);
        courseField.setToolTipText("Course Name");
        courseFields.add(courseField);
        
        JComboBox<String> creditCombo = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        creditComboBoxes.add(creditCombo);
        
        JComboBox<String> gradeCombo = new JComboBox<>(GRADE_POINTS.keySet().toArray(new String[0]));
        gradeComboBoxes.add(gradeCombo);
        
        coursePanel.add(new JLabel("Course:"));
        coursePanel.add(courseField);
        coursePanel.add(new JLabel("Credits:"));
        coursePanel.add(creditCombo);
        coursePanel.add(new JLabel("Grade:"));
        coursePanel.add(gradeCombo);
        
        coursesPanel.add(coursePanel);
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    private class CalculateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            
            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(GPACalculator.this, 
                    "Please enter student name and ID", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double totalPoints = 0;
            int totalCredits = 0;
            StringBuilder record = new StringBuilder();
            record.append("Student Name: ").append(name).append("\n");
            record.append("Student ID: ").append(id).append("\n");
            record.append("Courses:\n");
            
            for (int i = 0; i < courseFields.size(); i++) {
                String course = courseFields.get(i).getText().trim();
                if (course.isEmpty()) {
                    JOptionPane.showMessageDialog(GPACalculator.this, 
                        "Please enter all course names", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int credits = Integer.parseInt((String) creditComboBoxes.get(i).getSelectedItem());
                String grade = (String) gradeComboBoxes.get(i).getSelectedItem();
                double gradePoint = GRADE_POINTS.get(grade);
                
                totalPoints += gradePoint * credits;
                totalCredits += credits;
                
                record.append(String.format("- %s: %d credits, Grade %s (%.1f)\n", 
                    course, credits, grade, gradePoint));
            }
            
            if (totalCredits == 0) {
                JOptionPane.showMessageDialog(GPACalculator.this, 
                    "Please add at least one course", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double gpa = totalPoints / totalCredits;
            record.append(String.format("\nGPA: %.2f", gpa));
            
           
            JOptionPane.showMessageDialog(GPACalculator.this, 
                record.toString(), 
                "GPA Result", JOptionPane.INFORMATION_MESSAGE);
            
            
            try (FileWriter writer = new FileWriter("gpa_records.txt", true)) {
    writer.write(record.toString() + "\n\n");
    JOptionPane.showMessageDialog(GPACalculator.this, 
        "Data saved to gpa_records.txt", 
        "Success", JOptionPane.INFORMATION_MESSAGE);
} catch (IOException ex) {
    JOptionPane.showMessageDialog(GPACalculator.this, 
        "Error saving to file: " + ex.getMessage(), 
        "Error", JOptionPane.ERROR_MESSAGE);
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GPACalculator calculator = new GPACalculator();
            calculator.setVisible(true);
        });
    }
}