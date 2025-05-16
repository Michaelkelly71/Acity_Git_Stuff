import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class GPA {
    private static double calculateGPA(String creditHours, String grades) {
        String[] creditHoursArray = creditHours.split(",");
        String[] gradesArray = grades.split(",");

        if (creditHoursArray.length != gradesArray.length) {
            JOptionPane.showMessageDialog(null, "Mismatch between credit hours and grades count.");
            return 0.0;
        }

        double totalPoints = 0.0;
        double totalCreditHours = 0.0;

        for (int i = 0; i < creditHoursArray.length; i++) {
            try {
                double creditHour = Double.parseDouble(creditHoursArray[i].trim());
                double gradePoint = convertGradeToPoint(gradesArray[i].trim());

                totalPoints += gradePoint * creditHour;
                totalCreditHours += creditHour;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input in credit hours or grades.");
                return 0.0;
            }
        }

        return totalCreditHours == 0 ? 0.0 : totalPoints / totalCreditHours;
    }

    private static double convertGradeToPoint(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E+": return 1.0;
            case "F": return 0.0;
            default: 
                JOptionPane.showMessageDialog(null, "Invalid grade: " + grade);
                return 0.0;
        }
    }

    private static void saveToFile(String name, String id, String courses, String creditHours, String grades, double gpa) {
        try (FileWriter writer = new FileWriter("gpa_records.txt", true)) {
            writer.write("Name: " + name + ", ID: " + id + ", Courses: " + courses + ", Credit Hours: " + creditHours + ", Grades: " + grades + ", GPA: " + gpa + "\n");
            JOptionPane.showMessageDialog(null, "Saved to file successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving to file: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GPA Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        panel.setBackground(new Color(230, 240, 255)); 

        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField nameField = new JTextField();
        nameField.setToolTipText("Enter the student's full name");
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField idField = new JTextField();
        idField.setToolTipText("Enter the student's ID");
        panel.add(idLabel);
        panel.add(idField);

        JLabel coursesLabel = new JLabel("Courses:");
        coursesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField coursesField = new JTextField();
        coursesField.setToolTipText("Enter the courses separated by commas");
        panel.add(coursesLabel);
        panel.add(coursesField);

        JLabel creditHoursLabel = new JLabel("Credit Hours:");
        creditHoursLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField creditHoursField = new JTextField();
        creditHoursField.setToolTipText("Enter the credit hours separated by commas");
        panel.add(creditHoursLabel);
        panel.add(creditHoursField);

        JLabel gradesLabel = new JLabel("Grades:");
        gradesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField gradesField = new JTextField();
        gradesField.setToolTipText("Enter the grades separated by commas");
        panel.add(gradesLabel);
        panel.add(gradesField);

        JLabel saveOptionLabel = new JLabel("Save Option:");
        saveOptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JComboBox<String> saveOptionComboBox = new JComboBox<>(new String[]{"File", "Database"});
        saveOptionComboBox.setToolTipText("Choose where to save the GPA record");
        panel.add(saveOptionLabel);
        panel.add(saveOptionComboBox);

        JButton calculateButton = new JButton("Calculate and Save");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        calculateButton.setBackground(new Color(100, 149, 237)); 
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setToolTipText("Click to calculate GPA and save the record");
        panel.add(calculateButton);

        frame.add(panel);
        frame.setVisible(true);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                String courses = coursesField.getText();
                String creditHours = creditHoursField.getText();
                String grades = gradesField.getText();

                double gpa = calculateGPA(creditHours, grades);

                saveToFile(name, id, courses, creditHours, grades, gpa);
            }
        });
    }
}