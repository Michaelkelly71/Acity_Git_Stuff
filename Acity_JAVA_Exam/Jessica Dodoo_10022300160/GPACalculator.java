import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;

public class GPACalculator extends JFrame {
    private JTextField nameField, idField, courseField, creditField;
    private JComboBox<String> gradeCombo, storageCombo;
    private DefaultTableModel tableModel, historyTableModel;
    private ArrayList<String> grades = new ArrayList<>();
    private ArrayList<Integer> credits = new ArrayList<>();
    private ArrayList<String> courses = new ArrayList<>();
    private ArrayList<String[]> historyList = new ArrayList<>();

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        Font font = new Font("Arial", Font.PLAIN, 14);

        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        topPanel.setBackground(Color.WHITE);
        nameField = new JTextField();
        idField = new JTextField();
        topPanel.add(new JLabel("Student Full Name:"));
        topPanel.add(nameField);
        topPanel.add(new JLabel("Student ID:"));
        topPanel.add(idField);

       
        JPanel coursePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        coursePanel.setBackground(Color.WHITE);
        courseField = new JTextField(10);
        creditField = new JTextField(5);
        gradeCombo = new JComboBox<>(new String[] { "A", "B+", "B", "C+", "C", "D", "E", "F" });
        JButton addCourseBtn = new JButton("Add Course");
        coursePanel.add(new JLabel("Course Name:"));
        coursePanel.add(courseField);
        coursePanel.add(new JLabel("Credit Hours:"));
        coursePanel.add(creditField);
        coursePanel.add(new JLabel("Grade:"));
        coursePanel.add(gradeCombo);
        coursePanel.add(addCourseBtn);

        
        tableModel = new DefaultTableModel(new String[] { "Course", "Credit Hours", "Grade" }, 0);
        JTable courseTable = new JTable(tableModel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        storageCombo = new JComboBox<>(new String[] { "File", "Database" });
        JButton calculateBtn = new JButton("Calculate");
        JButton deleteBtn = new JButton("Delete");
        JButton updateBtn = new JButton("Update");
        JButton viewHistoryBtn = new JButton("View History");
        JButton addNewBtn = new JButton("Add New");

        JButton[] buttons = { addCourseBtn, calculateBtn, deleteBtn, updateBtn, viewHistoryBtn, addNewBtn };
        for (JButton btn : buttons) {
            btn.setFont(font);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setForeground(Color.BLACK);
            btn.setPreferredSize(new Dimension(100, 30));
        }

        bottomPanel.add(new JLabel("Store Results In:"));
        bottomPanel.add(storageCombo);
        for (JButton btn : buttons)
            bottomPanel.add(btn);

       
        addCourseBtn.addActionListener(e -> {
            try {
                String course = courseField.getText();
                String grade = (String) gradeCombo.getSelectedItem();
                int credit = Integer.parseInt(creditField.getText());

                if (course.isEmpty() || credit <= 0) {
                    JOptionPane.showMessageDialog(this, "Enter valid course name and credit hours.");
                    return;
                }

                tableModel.addRow(new Object[] { course, credit, grade });
                courses.add(course);
                grades.add(grade);
                credits.add(credit);
                courseField.setText("");
                creditField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credit hours must be a number.");
            }
        });

       
        deleteBtn.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                courses.remove(selectedRow);
                grades.remove(selectedRow);
                credits.remove(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Select a course to delete.");
            }
        });

       
        updateBtn.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow != -1) {
                String newCourse = JOptionPane.showInputDialog("Enter new course name:");
                String newGrade = (String) JOptionPane.showInputDialog(this, "Select new grade:", "Update Grade",
                        JOptionPane.PLAIN_MESSAGE, null, new String[] { "A", "B+", "B", "C+", "C", "D", "E", "F" },
                        tableModel.getValueAt(selectedRow, 2));
                String newCredits = JOptionPane.showInputDialog("Enter new credit hours:");
                try {
                    int newCredit = Integer.parseInt(newCredits);
                    tableModel.setValueAt(newCourse, selectedRow, 0);
                    tableModel.setValueAt(newCredit, selectedRow, 1);
                    tableModel.setValueAt(newGrade, selectedRow, 2);
                    courses.set(selectedRow, newCourse);
                    grades.set(selectedRow, newGrade);
                    credits.set(selectedRow, newCredit);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input for credits.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a course to update.");
            }
        });

       
        calculateBtn.addActionListener(e -> {
            String name = nameField.getText();
            String id = idField.getText();

            if (name.isEmpty() || id.isEmpty() || tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Enter all required student info and at least one course.");
                return;
            }

            double gpa = 0.0;
            int totalCredits = 0;
            double totalPoints = 0;
            for (int i = 0; i < grades.size(); i++) {
                int credit = credits.get(i);
                totalCredits += credit;
                switch (grades.get(i)) {
                    case "A":
                        totalPoints += 4.0 * credit;
                        break;
                    case "B+":
                        totalPoints += 3.5 * credit;
                        break;
                    case "B":
                        totalPoints += 3.0 * credit;
                        break;
                    case "C+":
                        totalPoints += 2.5 * credit;
                        break;
                    case "C":
                        totalPoints += 2.0 * credit;
                        break;
                    case "D":
                        totalPoints += 1.5 * credit;
                        break;
                    case "E":
                        totalPoints += 1.0 * credit;
                        break;
                    case "F":
                        totalPoints += 0.0;
                        break;
                }
            }

            gpa = totalPoints / totalCredits;
            String result = "Name: " + name + "\nID: " + id + "\nCourses: " + courses + "\nGPA: "
                    + String.format("%.2f", gpa);
            JOptionPane.showMessageDialog(this, "GPA: " + String.format("%.2f", gpa));
            historyList.add(new String[] { name, id, courses.toString(), String.format("%.2f", gpa) });

            String storageOption = (String) storageCombo.getSelectedItem();
            if ("File".equals(storageOption)) {
                try {
                    FileWriter writer = new FileWriter("gpa_records.txt", true);
                    writer.write(result + "\n\n");
                    writer.close();
                    JOptionPane.showMessageDialog(this, "Results saved to gpa_records.txt.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving to file: " + ex.getMessage());
                }
            } else if ("Database".equals(storageOption)) {
                JOptionPane.showMessageDialog(this, "Database storage is under construction.");
            }
        });

        
        viewHistoryBtn.addActionListener(e -> {
            String[] columns = { "Name", "ID", "Courses", "GPA" };
            historyTableModel = new DefaultTableModel(columns, 0);
            for (String[] record : historyList)
                historyTableModel.addRow(record);
            JTable historyTable = new JTable(historyTableModel);
            JScrollPane scroll = new JScrollPane(historyTable);
            JOptionPane.showMessageDialog(this, scroll, "History", JOptionPane.PLAIN_MESSAGE);
        });

       
        addNewBtn.addActionListener(e -> {
            nameField.setText("");
            idField.setText("");
            courseField.setText("");
            creditField.setText("");
            tableModel.setRowCount(0);
            courses.clear();
            grades.clear();
            credits.clear();
        });

       
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(coursePanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GPACalculator::new);
    }
}
