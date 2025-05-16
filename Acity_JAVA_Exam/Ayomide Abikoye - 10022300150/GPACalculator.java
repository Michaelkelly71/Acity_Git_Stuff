import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class GPACalculator extends JFrame implements ActionListener {

    private static final Color COLOR_BACKGROUND = new Color(240, 245, 250);
    private static final Color COLOR_PRIMARY_BORDER = new Color(180, 190, 200);
    private static final Color COLOR_BUTTON = new Color(70, 130, 180);
    private static final Color COLOR_BUTTON_TEXT = Color.WHITE;
    private static final Color COLOR_GPA_SUCCESS = new Color(0, 100, 0);
    private static final Color COLOR_GPA_ERROR = Color.RED;
    private static final Color COLOR_TEXT_FIELD_BG = Color.WHITE;

    private static final Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_FIELD = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_GPA = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 14);

    private static final String SAVE_TO_DATABASE = "Database";
    private static final String SAVE_TO_FILE = "File (Not Implemented)";
    private static final String[] SAVE_OPTIONS = { SAVE_TO_DATABASE, SAVE_TO_FILE };

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/GPA";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "SYSTEM";

    private static class Course {
        final String name;
        final int credits;
        JTextField gradeField;

        Course(String name, int credits) {
            this.name = name;
            this.credits = credits;
            this.gradeField = new JTextField(5);
            this.gradeField.setFont(FONT_FIELD);
            this.gradeField.setHorizontalAlignment(JTextField.CENTER);
            this.gradeField.setBackground(COLOR_TEXT_FIELD_BG);
            this.gradeField.setBorder(BorderFactory.createCompoundBorder(
                    this.gradeField.getBorder(),
                    new EmptyBorder(2, 4, 2, 4)));
        }
    }

    private final Course[] courses = {
            new Course("OOP with Java", 3),
            new Course("Data Structures and Algorithms", 3),
            new Course("Applied Linear Algebra", 3),
            new Course("African Studies", 2),
            new Course("Computer Architecture", 3),
            new Course("FIE II", 2),
            new Course("Probability,Statistics & Reliability", 3)
    };

    private JTextField studentNameField;
    private JTextField studentIdField;
    private JLabel gpaResultLabel;
    private JComboBox<String> saveOptionsComboBox;
    private JButton calculateButton;
    private JButton saveButton;

    private double calculatedGpa = -1.0;

    public GPACalculator() {
        super("Simple GPA Calculator");
        initializeUI();
        initializeDatabase();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(COLOR_BACKGROUND);

        studentNameField = new JTextField(20);
        studentNameField.setFont(FONT_FIELD);
        studentNameField.setBackground(COLOR_TEXT_FIELD_BG);

        studentIdField = new JTextField(12);
        studentIdField.setFont(FONT_FIELD);
        studentIdField.setBackground(COLOR_TEXT_FIELD_BG);

        gpaResultLabel = new JLabel("GPA: N/A", SwingConstants.CENTER);
        gpaResultLabel.setFont(FONT_GPA);
        gpaResultLabel.setOpaque(true);
        gpaResultLabel.setBackground(Color.WHITE);
        gpaResultLabel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_PRIMARY_BORDER, 1),
                new EmptyBorder(5, 10, 5, 10)));

        saveOptionsComboBox = new JComboBox<>(SAVE_OPTIONS);
        saveOptionsComboBox.setFont(FONT_LABEL);
        saveOptionsComboBox.setBackground(COLOR_TEXT_FIELD_BG);

        calculateButton = createSimpleButton("Calculate GPA");
        saveButton = createSimpleButton("Save Data");

        calculateButton.addActionListener(this);
        saveButton.addActionListener(this);

        JPanel studentInfoPanel = createStudentInfoPanel();
        JPanel coursesPanel = createCoursesPanel();
        JPanel actionsPanel = createActionsPanel();

        mainPanel.add(studentInfoPanel, BorderLayout.NORTH);
        mainPanel.add(coursesPanel, BorderLayout.CENTER);
        mainPanel.add(actionsPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setMinimumSize(new Dimension(650, 550));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        return label;
    }

    private JButton createSimpleButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(COLOR_BUTTON);
        button.setForeground(COLOR_BUTTON_TEXT);
        button.setFocusPainted(false);
        button.setMargin(new Insets(5, 12, 5, 12));
        return button;
    }

    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY_BORDER, 1), title);
        border.setTitleFont(FONT_TITLE);
        border.setTitleColor(COLOR_BUTTON);
        return border;
    }

    private JPanel createStudentInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(createTitledBorder("Student Information"));
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(createLabel("Student Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(studentNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(createLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(studentIdField, gbc);

        return panel;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(createTitledBorder("Course Grades"));
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel courseHeader = createLabel("Course");
        courseHeader.setFont(FONT_LABEL.deriveFont(Font.BOLD));
        JLabel creditsHeader = createLabel("Credits");
        creditsHeader.setFont(FONT_LABEL.deriveFont(Font.BOLD));
        creditsHeader.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel gradeHeader = createLabel("Grade (A, B+)");
        gradeHeader.setFont(FONT_LABEL.deriveFont(Font.BOLD));
        gradeHeader.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(courseHeader, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(creditsHeader, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(gradeHeader, gbc);

        for (int i = 0; i < courses.length; i++) {
            gbc.gridy = i + 1;

            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(createLabel(courses[i].name), gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.NONE;
            JLabel creditLabel = createLabel(String.valueOf(courses[i].credits));
            creditLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(creditLabel, gbc);

            gbc.gridx = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            courses[i].gradeField.setToolTipText("Enter grade for " + courses[i].name);
            panel.add(courses[i].gradeField, gbc);
        }
        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel actionsPanel = new JPanel(new BorderLayout(20, 5));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(calculateButton);
        actionsPanel.add(leftPanel, BorderLayout.WEST);

        actionsPanel.add(gpaResultLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(createLabel("Save To:"));
        rightPanel.add(saveOptionsComboBox);
        rightPanel.add(saveButton);
        actionsPanel.add(rightPanel, BorderLayout.EAST);

        return actionsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calculateButton) {
            calculateGpaAction();
        } else if (e.getSource() == saveButton) {
            saveDataAction();
        }
    }

    private void calculateGpaAction() {
        double totalQualityPoints = 0;
        double totalCreditHours = 0;
        calculatedGpa = -1.0;
        boolean errorFound = false;

        try {
            for (Course course : courses) {
                String gradeStr = course.gradeField.getText().trim().toUpperCase();

                if (gradeStr.isEmpty()) {
                    continue;
                }

                double credits = course.credits;
                double gradePoint = gradeToPoint(gradeStr);

                if (gradePoint < 0) {
                    showError("Invalid grade '" + course.gradeField.getText().trim() + "' for course '" + course.name
                            + "'. Use A, B+, C, etc.");
                    errorFound = true;
                    break;
                }

                totalQualityPoints += credits * gradePoint;
                totalCreditHours += credits;
            }

            if (errorFound) {
                gpaResultLabel.setText("GPA: Error");
                gpaResultLabel.setForeground(COLOR_GPA_ERROR);
                calculatedGpa = -1.0;
            } else if (totalCreditHours == 0) {
                showWarning("No grades entered. Cannot calculate GPA.");
                gpaResultLabel.setText("GPA: N/A");
                gpaResultLabel.setForeground(Color.BLACK);
            } else {
                calculatedGpa = totalQualityPoints / totalCreditHours;
                gpaResultLabel.setText(String.format("GPA: %.2f", calculatedGpa));
                gpaResultLabel.setForeground(COLOR_GPA_SUCCESS);

            }

        } catch (Exception ex) {
            showError("An unexpected error occurred during calculation: " + ex.getMessage());
            gpaResultLabel.setText("GPA: Error");
            gpaResultLabel.setForeground(COLOR_GPA_ERROR);
            calculatedGpa = -1.0;
            ex.printStackTrace();
        }
    }

    private void saveDataAction() {
        String selectedOption = (String) saveOptionsComboBox.getSelectedItem();
        String studentName = studentNameField.getText().trim();
        String studentId = studentIdField.getText().trim();

        if (studentName.isEmpty() || studentId.isEmpty()) {
            showError("Please enter Student Name and ID before saving.");
            return;
        }

        if (calculatedGpa < 0) {
            boolean gradesEntered = false;
            for (Course c : courses) {
                if (!c.gradeField.getText().trim().isEmpty()) {
                    gradesEntered = true;
                    break;
                }
            }
            if (gradesEntered || gpaResultLabel.getText().equals("GPA: Error")) {
                showError("Please calculate a valid GPA before saving.");
            } else {
                showError("Please enter grades and calculate GPA before saving.");
            }
            return;
        }

        if (SAVE_TO_DATABASE.equals(selectedOption)) {
            saveToDatabase(studentId, studentName, calculatedGpa);
        } else if (SAVE_TO_FILE.equals(selectedOption)) {
            showWarning("Saving to file is not yet implemented.");
        }
    }

    private double gradeToPoint(String grade) {
        Map<String, Double> gradeMap = new HashMap<>();
        gradeMap.put("A", 4.0);
        gradeMap.put("B+", 3.5);
        gradeMap.put("B", 3.0);
        gradeMap.put("C+", 2.5);
        gradeMap.put("C", 2.0);
        gradeMap.put("D", 1.5);
        gradeMap.put("E", 1.0);
        gradeMap.put("F", 0.0);

        return gradeMap.getOrDefault(grade, -1.0);
    }

    private void saveToDatabase(String studentId, String studentName, double gpa) {
        String sql = "INSERT INTO StudentGPA (StudentID, StudentName, GPA) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE StudentName = VALUES(StudentName), GPA = VALUES(GPA)"; 

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException cnfe) {
            showError("MariaDB JDBC Driver not found. Please add the driver JAR to your project's classpath.");
            cnfe.printStackTrace();
            return; 
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, studentId);
            pstmt.setString(2, studentName);
            pstmt.setDouble(3, gpa);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                showMessage("Data saved/updated successfully in database for Student ID: " + studentId);
            } else {
                showMessage("Data processed for Student ID: " + studentId
                        + ". (No changes detected or record already up-to-date)");
            }

        } catch (SQLException se) {
            showError("Database Error: " + se.getMessage() + "\nSQL State: " + se.getSQLState() + "\nError Code: "
                    + se.getErrorCode() +
                    "\n\nEnsure DB server is running, 'GPA' database exists, and credentials are correct.");
            se.printStackTrace(); 
        } catch (Exception ex) {
            showError("An unexpected error occurred during database operation: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se) {
                /* ignored */ }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS StudentGPA (" +
                " StudentID VARCHAR(50) PRIMARY KEY NOT NULL," +
                " StudentName VARCHAR(100) NOT NULL," +
                " GPA DOUBLE PRECISION NOT NULL" +
                ");";
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("MariaDB JDBC Driver not found during initialization. Database features unavailable.");
            
            return; 
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Database connection successful. Table 'StudentGPA' checked/created.");

        } catch (SQLException e) {
            System.err.println("Error initializing database connection or creating table: " + e.getMessage());
            showError("Could not connect to database: " + e.getMessage() +
                    "\nPlease ensure the MariaDB server is running, the 'GPA' database exists, " +
                    "credentials are correct, and the JDBC driver is in the classpath.");
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Unexpected error during database initialization: " + ex.getMessage());
            showError("Unexpected error during database initialization: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GPACalculator());
    }
}
