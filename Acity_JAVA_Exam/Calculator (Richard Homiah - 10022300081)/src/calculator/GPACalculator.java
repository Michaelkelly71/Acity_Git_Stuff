package calculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class GPACalculator extends JFrame {

    // UI Components
    JTextField nameField, idField;
    JTable courseTable;
    JComboBox<String> saveBox;
    JButton calcButton;

    public GPACalculator() {
        // This part will be done in the JFrame GUI form
        // Example: nameField = new JTextField();
        //          calcButton = new JButton("Calculate GPA");

        // But if coding manually:
        setTitle("GPA Calculator");
        setSize(600, 400);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Text Fields
        nameField = new JTextField();
        nameField.setBounds(100, 20, 150, 25);
        add(new JLabel("Name:")).setBounds(20, 20, 50, 25);
        add(nameField);

        idField = new JTextField();
        idField.setBounds(100, 60, 150, 25);
        add(new JLabel("ID:")).setBounds(20, 60, 50, 25);
        add(idField);

        // Table for Course, Credit, Grade
        String[] columns = {"Course", "Credit Hours", "Grade (A-F)"};
        courseTable = new JTable(new DefaultTableModel(columns, 5));
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBounds(20, 100, 540, 100);
        add(scrollPane);

        // ComboBox
        saveBox = new JComboBox<>(new String[]{"Save to File", "Save to Database"});
        saveBox.setBounds(20, 220, 150, 25);
        add(saveBox);

        // Button
        calcButton = new JButton("Calculate GPA");
        calcButton.setBounds(200, 220, 150, 25);
        add(calcButton);

        // Action
        calcButton.addActionListener(e -> calculateGPA());

        setVisible(true);
    }

    void calculateGPA() {
        String name = nameField.getText();
        String id = idField.getText();

        double totalPoints = 0;
        double totalCredits = 0;

        // Loop through table rows
        for (int i = 0; i < courseTable.getRowCount(); i++) {
            Object creditObj = courseTable.getValueAt(i, 1);
            Object gradeObj = courseTable.getValueAt(i, 2);

            if (creditObj == null || gradeObj == null) continue;

            double credit = Double.parseDouble(creditObj.toString());
            String grade = gradeObj.toString().toUpperCase();

            double gradePoint = switch (grade) {
                case "A" -> 4.0;
                case "B" -> 3.0;
                case "C" -> 2.0;
                case "D" -> 1.0;
                default -> 0.0;
            };

            totalPoints += gradePoint * credit;
            totalCredits += credit;
        }

        double gpa = totalCredits == 0 ? 0 : totalPoints / totalCredits;

        JOptionPane.showMessageDialog(this, "GPA: " + gpa);

        String saveOption = (String) saveBox.getSelectedItem();
        if (saveOption.equals("Save to File")) {
            try {
                FileWriter writer = new FileWriter("gpa_records.txt", true);
                writer.write("Name: " + name + ", ID: " + id + ", GPA: " + gpa + "\n");
                writer.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving file");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database: Under Construction");
        }
    }

    public static void main(String[] args) {
        new GPACalculator();
    }
}
