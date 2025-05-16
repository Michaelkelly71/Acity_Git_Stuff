import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GPACalculator extends JFrame {

    private JTextField nameField, idField, courseField, creditField, gradeField;
    private JTextArea outputArea;
    private JComboBox<String> saveOption;

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 2));

        add(new JLabel("Student Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Student ID:"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("Courses (comma separated):"));
        courseField = new JTextField();
        add(courseField);

        add(new JLabel("Credit Hours (comma separated):"));
        creditField = new JTextField();
        add(creditField);

        add(new JLabel("Grades (comma separated):"));
        gradeField = new JTextField();
        add(gradeField);

        add(new JLabel("Save to:"));
        saveOption = new JComboBox<>(new String[]{"File", "Database"});
        add(saveOption);

        JButton calcButton = new JButton("Calculate GPA");
        add(calcButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane);

        calcButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateAndSave();
            }
        });
    }

    private void calculateAndSave() {
        try {
            String name = nameField.getText();
            String id = idField.getText();
            String[] courses = courseField.getText().split(",");
            String[] credits = creditField.getText().split(",");
            String[] grades = gradeField.getText().split(",");

            if (courses.length != credits.length || grades.length != credits.length) {
                outputArea.setText("Mismatch in number of courses, credits, or grades.");
                return;
            }

            double gpa = GPACalculatorLogic.calculateGPA(credits, grades);
            String choice = saveOption.getSelectedItem().toString();

            if (choice.equals("File")) {
                GPASaver.saveToFile(name, id, courses, grades, credits, gpa);
                outputArea.setText("GPA: " + String.format("%.2f", gpa) + "\nSaved to file.");
            } else {
                outputArea.setText("Database saving is not available.");
            }
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        GPACalculator app = new GPACalculator();
        app.setVisible(true);
    }
}
