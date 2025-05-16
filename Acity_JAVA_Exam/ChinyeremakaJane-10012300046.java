import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.*.event.*;
import java.io.filewriter;
import java.io.IOExpection;

public class GpaCalculator extends JFrame{
    private JTextField nameField, idField,CoursesField;
    private JTable CourseTable;
    private JComboBox<String> storageComboBox;
    private JButton CalculateButton;
    private JLabel resultLabel;
      

    public Gpacalculator(){
        setTitle("Students GpaCalculator");
        setSize(700,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        selectLOcationRelativeTo(null);
        setlayout(new BoarderLayout(10,10));


        JPanel topPanel= new JPanel(new GridLayout(2,2,5,5));
        topPanel.setBoarder(BorderFactory.createTitleBorder("student info"));
        topPanel.add(new JLabel("Name:"));
        nameField= new JTextField();
        topPanel.add(namefield);

        topPanel.add(new JLabel("ID"));
        idField= new JTextField();
        topPanel.add(idField);

        add(topPanel,BorderLayout.NORTH)

        String[]Columns ={"Course name","Credit Hours","Grade (A-F)"};
        DefaultTableModel model = new DefaultTableModel(columns,5);
        courseTable =new JTable(model);
        JScrollpane tableScroll = new JscrollPane(courseTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Courses"));

        add(tableSroll,BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new Gridlayout(3,2,5,5));
        bottomPanel.setBorder(BorderFactory.createTiltedBorder("Controls"));

        bottomPanel.add(new JLabel("Save to:"));
        storageComboBox = new JCombo<>(new String[]{"file","Database"})
        bottomPanel. add(storageComboBox);

        CalculateButton = new JButton("Calculate Gpa");
        buttomPanel.add(CalculateButton);

        ResultLabel = new JLabel("Gpa:")
        buttomPanel.add(resultLabel);
        add(buttomPanel,BorderLayout.SOUTH);

        CalculateButton.actionlistener(e-> CalculateandsaveGPA());
    }
    private void calculateAndSaveGPA(){
        String name = nameField.getText();
        String Id = idField.getText();

        if (name.isEmpty() ||id.isEmpty()){
            JOptionpane.ShowMessageDialog(this,"please enter your name and ID.");
            return;
        
        }
          double totalPoints = 0;
          double totalCredits = 0;

          for (int i =0; i< Coursetable.getRowCount());
               Object CourseObj = courseTable.getValueAt(i,0);
               Object CreditObj = creditTable.getValueAt(i,1);
               Object Gradeobj = GradeTable.getvaluat(i,2);
               if (CourseObj == null || CreditObj == null ||GradeObj ==null ) continue;
               try {
                double credits = Double.parseDouble(creditObj.toString());
                double gradePoint = gradeToPoint(gradeObj.toString().toUpperCase());
                totalPoints += credits * gradePoint;
                totalCredits += credits;
               } catch(Expeption ex){
                JOptionpane.showMessageDialog(this,"Error in row" + (i+1));
                return;
               }
 }
 if (totalCredits == 0){
     JOptionpane.showMessageDialog(this,"No vaild credit hours.");
     return;
 }
 double gpa = totalPoints/ totalCredits;
 resultLabel.setText(String.format("GPA: %.2f",gpa));

 String choice = (String) storageComboBox.getSelectedItem();
 if (choice.equals("File")) {
    saveTOFile(name,id,gpa);
  } else {
    JOptionpane.showMessageDialog(this,"Database: Under Construction.");
 }


 private double gradeTOPoint(String grade) {
    return switch(grade) {
        case "A" -> 4.0;
        case "B+" -> 3.5;
        case "B" -> 3.0;
        case "C" -> 2.5;
        case "C+" -> 2.0;
        case "D" -> 1.5;
        case "E" -> 1.0;
        case  "F" -> 0.0;
        default -> throw new IllegalArgumentException("Invalid grade");
    };
}
private void saveTOFile(String name,String id, double gpa) {
    try (FileWriter fw = new filewriter("gpa_records.txt",true)) {
       fw.write("Name:"+ name +",ID:"+ id +",GPA"+ String.format("%.2f",gpa)+"\n");
        JOptionpane.showMessageDialog(this,"saved to file");
    }catch (IOException e) {
        JOptionPane.showMessageDialog(this,"File save error.");
    }
}
public static void main(String[] args) {
    SwingUtilities.invokeLater(()-> new GpaCalculator().setVisible(true));
}
}
 
