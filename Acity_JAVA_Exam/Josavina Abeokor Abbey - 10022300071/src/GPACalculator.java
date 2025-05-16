/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author HP
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.util.ArrayList;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField;
    private JComboBox<String> storageOption;
    private JPanel coursesPanel;
    private JButton addCourseBtn, calculateBtn;
    private ArrayList<JTextField> courseFields = new ArrayList<>();
    private ArrayList<JTextField> creditFields = new ArrayList<>();
    private ArrayList<JComboBox<String>> gradeFields = new ArrayList<>();

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Student Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 2));
        infoPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        infoPanel.add(nameField);

        infoPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        infoPanel.add(idField);

        infoPanel.add(new JLabel("Save Option:"));
        storageOption = new JComboBox<>(new String[]{"File", "Database"});
        infoPanel.add(storageOption);

        add(infoPanel, BorderLayout.NORTH);

        // Course Panel
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        addCourseRow(); // Add one row by default

        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Courses"));
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        addCourseBtn = new JButton("Add Course");
        calculateBtn = new JButton("Calculate GPA");

        buttonPanel.add(addCourseBtn);
        buttonPanel.add(calculateBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addCourseBtn.addActionListener(e -> addCourseRow());
        calculateBtn.addActionListener(e -> calculateGPA());

        setVisible(true);
    }

    private void addCourseRow() {
        JPanel row = new JPanel(new FlowLayout());
        JTextField courseField = new JTextField(10);
        JTextField creditField = new JTextField(5);
        JComboBox<String> gradeBox = new JComboBox<>(new String[]{"A", "B+", "B", "C+", "C", "D", "E", "F"});

        row.add(new JLabel("Course:"));
        row.add(courseField);
        row.add(new JLabel("Credit:"));
        row.add(creditField);
        row.add(new JLabel("Grade:"));
        row.add(gradeBox);

        courseFields.add(courseField);
        creditFields.add(creditField);
        gradeFields.add(gradeBox);

        coursesPanel.add(row);
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    private void calculateGPA() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();

        if (name.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter student name and ID.");
            return;
        }

        double totalPoints = 0;
        double totalCredits = 0;

        for (int i = 0; i < courseFields.size(); i++) {
            try {
                String course = courseFields.get(i).getText().trim();
                double credit = Double.parseDouble(creditFields.get(i).getText().trim());
                String grade = (String) gradeFields.get(i).getSelectedItem();

                double gradePoint = getGradePoint(grade);
                totalPoints += gradePoint * credit;
                totalCredits += credit;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input in one of the rows.");
                return;
            }
        }

        if (totalCredits == 0) {
            JOptionPane.showMessageDialog(this, "No credit hours entered.");
            return;
        }

        double gpa = totalPoints / totalCredits;
        String saveOption = (String) storageOption.getSelectedItem();

        if (saveOption.equals("File")) {
            try (FileWriter writer = new FileWriter("gpa_records.txt", true)) {
                writer.write("Name: " + name + ", ID: " + id + ", GPA: " + String.format("%.2f", gpa) + "\n");
                JOptionPane.showMessageDialog(this, "GPA: " + String.format("%.2f", gpa) +
                        "\nSaved to gpa_records.txt\nDatabase: Under Construction.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error writing to file.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "GPA: " + String.format("%.2f", gpa) +
                    "\nFile: Yet to be Implemented.\nDatabase: Under Construction.");
        }
    }

    private double getGradePoint(String grade) {
        return switch (grade) {
            case "A" -> 4.0;
            case "B+" -> 3.5;
            case "B" -> 3.0;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "D" -> 1.5;
            case "E" -> 1.0;
            case "F" -> 0.0;
            default -> 0.0;
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GPACalculator::new);
    }
}


