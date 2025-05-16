import java.awt.*;
import java.io.*;
import javax.swing.*;

public class SimpleGPA_UI extends JFrame {
    private JTextField nameField, idField, courseField, creditField, gradeField, semesterField;
    private JTextArea output;
    private JComboBox<String> saveOption;

    public SimpleGPA_UI() {
        setTitle("GPA Calculator");
        setSize(600, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Color theme: light navy blue
        Color background = new Color(180, 198, 231);  // Light navy
        Color buttonColor = new Color(25, 45, 85);    // Dark navy
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font titleFont = new Font("Arial", Font.BOLD, 18);

        // Title at the top
        JLabel heading = new JLabel("Simple GPA Calculator", JLabel.CENTER);
        heading.setFont(titleFont);
        heading.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        heading.setBackground(background);
        heading.setOpaque(true);
        add(heading, BorderLayout.NORTH);

        // Form input panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        formPanel.setBackground(background);

        nameField = createInput(formPanel, "Student Name:");
        idField = createInput(formPanel, "Student ID:");
        semesterField = createInput(formPanel, "Semester (e.g. Fall 2024):");
        courseField = createInput(formPanel, "Courses (comma):");
        creditField = createInput(formPanel, "Credit Hours (comma):");
        gradeField = createInput(formPanel, "Grades (comma):");

        formPanel.add(new JLabel("Save Method:", JLabel.RIGHT));
        saveOption = new JComboBox<>(new String[]{"File", "Database"});
        formPanel.add(saveOption);

        add(formPanel, BorderLayout.CENTER);

        // Bottom panel with button and output
        output = new JTextArea(6, 40);
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        output.setBackground(Color.WHITE); // Good contrast
        JScrollPane scrollPane = new JScrollPane(output);

        JButton calcBtn = new JButton("Calculate GPA");
        calcBtn.setBackground(buttonColor);
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setFont(labelFont);
        calcBtn.setFocusPainted(false);
        calcBtn.addActionListener(e -> calculateGPA());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        bottomPanel.setBackground(background);
        bottomPanel.add(calcBtn, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // Entire background
        getContentPane().setBackground(background);
        setVisible(true);
    }

    private JTextField createInput(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText, JLabel.RIGHT));
        JTextField field = new JTextField();
        panel.add(field);
        return field;
    }

    private void calculateGPA() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String semester = semesterField.getText().trim();
        String[] courses = courseField.getText().split(",");
        String[] credits = creditField.getText().split(",");
        String[] grades = gradeField.getText().split(",");

        if (courses.length != credits.length || credits.length != grades.length) {
            output.setText("Error: Mismatch in number of courses, credits, or grades.");
            return;
        }

        try {
            double totalPoints = 0;
            int totalCredits = 0;

            for (int i = 0; i < grades.length; i++) {
                int credit = Integer.parseInt(credits[i].trim());
                double gradePoint = convertToPoint(grades[i].trim());
                totalPoints += gradePoint * credit;
                totalCredits += credit;
            }

            double gpa = totalPoints / totalCredits;

            String result = "Student: " + name + "\n" +
                            "ID: " + id + "\n" +
                            "Semester: " + semester + "\n" +
                            "GPA: " + String.format("%.2f", gpa) + "\n";

            if (saveOption.getSelectedItem().equals("File")) {
                saveToFile(result);
            } else {
                output.setText("Database saving is not yet implemented.");
            }

        } catch (NumberFormatException e) {
            output.setText("Error: Credit hours must be numbers.");
        }
    }

    private double convertToPoint(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "D": return 1.5;
            case "E": return 1.0;
            default: return 0.0;
        }
    }

    private void saveToFile(String text) {
        try (FileWriter writer = new FileWriter("gpa_records.txt", true)) {
            writer.write(text + "\n----------------------------\n");
            output.setText("GPA saved to file successfully!\n\n" + text);
        } catch (IOException e) {
            output.setText("Failed to save GPA to file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleGPA_UI::new);
    }
}

