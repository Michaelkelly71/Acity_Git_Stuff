import java.awt.*;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;

public class GpaCalculator extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField studentNameField, studentIdField;
    private JButton addStudentButton, goToCoursesButton;
    private DefaultListModel<String> studentListModel;
    private Map<String, String> studentMap = new LinkedHashMap<>();

    private JComboBox<String> studentSelector;
    private JTextField[] courseFields, creditFields, gradeFields;
    private JComboBox<String> storageOption;
    private JTextArea resultArea;
    private JButton calculateButton, viewResultsButton;

    public GpaCalculator() {
        setTitle("GPA Calculator");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupStudentEntryPanel();   
        setupCourseEntryPanel();    

        add(mainPanel);
        setVisible(true);
    }

    private void setupStudentEntryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        studentNameField = new JTextField(15);
        studentIdField = new JTextField(15);
        addStudentButton = new JButton("Add Student");
        goToCoursesButton = new JButton(" GPA ");

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(studentNameField);
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(studentIdField);
        inputPanel.add(addStudentButton);
        inputPanel.add(goToCoursesButton);

        studentListModel = new DefaultListModel<>();
        JList<String> studentList = new JList<>(studentListModel);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(studentList), BorderLayout.CENTER);

        addStudentButton.addActionListener(e -> {
            String name = studentNameField.getText().trim();
            String id = studentIdField.getText().trim();
            if (!name.isEmpty() && !id.isEmpty()) {
                String display = name + " (" + id + ")";
                studentMap.put(display, id);
                studentListModel.addElement(display);
                studentNameField.setText("");
                studentIdField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, " The is no  student name and ID.");
            }
        });

        goToCoursesButton.addActionListener(e -> {
            if (studentMap.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Add at least one student .");
                return;
            }
            studentSelector.removeAllItems();
            for (String display : studentMap.keySet()) {
                studentSelector.addItem(display);
            }
            cardLayout.next(mainPanel);
        });

        mainPanel.add(panel, "StudentEntry");
    }

    private void setupCourseEntryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        
        JPanel topPanel = new JPanel(new GridLayout(3, 2));
        studentSelector = new JComboBox<>();
        storageOption = new JComboBox<>(new String[]{"File", "Database"});
        topPanel.add(new JLabel("Select Student:"));
        topPanel.add(studentSelector);
        topPanel.add(new JLabel("Storage Option:"));
        topPanel.add(storageOption);

        
        JPanel centerPanel = new JPanel(new GridLayout(7, 4));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Grades"));
        courseFields = new JTextField[6];
        creditFields = new JTextField[6];
        gradeFields = new JTextField[6];

        centerPanel.add(new JLabel("Course"));
        centerPanel.add(new JLabel("Credit Hours"));
        centerPanel.add(new JLabel("Grade (A–F)"));
        centerPanel.add(new JLabel(""));

        for (int i = 0; i < 6; i++) {
            courseFields[i] = new JTextField(10);
            creditFields[i] = new JTextField(5);
            gradeFields[i] = new JTextField(2);

            centerPanel.add(courseFields[i]);
            centerPanel.add(creditFields[i]);
            centerPanel.add(gradeFields[i]);
            centerPanel.add(new JLabel(""));
        }

        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        resultArea = new JTextArea(6, 60);
        resultArea.setEditable(false);
        calculateButton = new JButton("Calculate GPA");
        viewResultsButton = new JButton("View Results");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(calculateButton);
        buttonPanel.add(viewResultsButton);

        bottomPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        calculateButton.addActionListener(e -> calculateAndSave());
        viewResultsButton.addActionListener(e -> showSavedResults());

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(panel, "CourseEntry");
    }

    private void calculateAndSave() {
        String studentInfo = (String) studentSelector.getSelectedItem();
        if (studentInfo == null) return;

        String name = studentInfo.split(" \\(")[0];
        String id = studentMap.get(studentInfo);
        String storage = (String) storageOption.getSelectedItem();

        StringBuilder data = new StringBuilder();
        data.append("Name: ").append(name).append("\n");
        data.append("ID: ").append(id).append("\n");

        double totalPoints = 0, totalCredits = 0;
        data.append("Courses:\n");

        StringBuilder csvData = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            String course = courseFields[i].getText().trim();
            String creditStr = creditFields[i].getText().trim();
            String grade = gradeFields[i].getText().trim().toUpperCase();

            if (course.isEmpty() || creditStr.isEmpty() || grade.isEmpty()) continue;

            double credit;
            try {
                credit = Double.parseDouble(creditStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid credit in row " + (i + 1));
                return;
            }

            Double point = gradeToPoint(grade);
            if (point == null) {
                JOptionPane.showMessageDialog(this, "Invalid grade in row " + (i + 1));
                return;
            }

            totalPoints += point * credit;
            totalCredits += credit;

            data.append(course).append(" - ").append(credit).append(" credits - ").append(grade).append("\n");
            csvData.append(String.format("%s,%s,%s,%.1f,%s\n", name, id, course, credit, grade));
        }

        if (totalCredits == 0) {
            JOptionPane.showMessageDialog(this, "No valid course entries.");
            return;
        }

        double gpa = totalPoints / totalCredits;
        data.append("GPA: ").append(String.format("%.2f", gpa)).append("\n");
        resultArea.setText(data.toString());

        if ("File".equals(storage)) {
            saveToFile(data.toString());
            saveToCSV(csvData.toString(), name, id, gpa);
        } else {
            resultArea.append("\n File: Yet to be Implemented.");
        }
    }
    private void showSavedResults() {
        try (BufferedReader reader = new BufferedReader(new FileReader("gpa_records.txt"))) {
            StringBuilder results = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                results.append(line).append("\n");
            }

            JTextArea display = new JTextArea(results.toString(), 20, 60);
            display.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(display), "Saved GPA Records", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No saved records found.");
        }
    }

    private Double gradeToPoint(String grade) {
        return switch (grade) {
            case "A" ->  4.0;
            case "B+" -> 3.5;
            case "B" ->  3.0;
            case "C+" -> 2.5;
            case "C" ->  2.0;
            case "D" ->  1.5;
            case "E" ->  1.0;
            case "F" ->  0.0;
            default -> null;
        };
    }

    private void saveToFile(String content) {
        try (FileWriter fw = new FileWriter("gpa_records.txt", true)) {
            fw.write(content + "\n----------\n");
            JOptionPane.showMessageDialog(this, "Data saved to gpa_records.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file.");
        }
    }

    private void saveToCSV(String courseData, String name, String id, double gpa) {
        try (FileWriter csvWriter = new FileWriter("gpa_records.csv", true)) {
            csvWriter.write("Name,ID,Course,Credit Hours,Grade\n");
            csvWriter.write(courseData);
            csvWriter.write(String.format(",,,,GPA: %.2f\n", gpa));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving CSV file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GpaCalculator::new);
    }
}

    
