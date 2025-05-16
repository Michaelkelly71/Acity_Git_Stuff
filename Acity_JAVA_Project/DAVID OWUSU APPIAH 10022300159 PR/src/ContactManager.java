import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.ResultSet;

public class ContactManager extends JFrame {
    private JTextField nameField, phoneField, emailField;
    private JTextArea displayArea;

    public ContactManager() {
        setTitle("Contact Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

       
        JPanel topPanel = new JPanel(new GridLayout(3, 2));
        nameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();

        topPanel.add(new JLabel("Name:")); topPanel.add(nameField);
        topPanel.add(new JLabel("Phone:")); topPanel.add(phoneField);
        topPanel.add(new JLabel("Email:")); topPanel.add(emailField);

        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save");
        JButton deleteBtn = new JButton("Delete");
        JButton sortNameBtn = new JButton("Sort by Name");
        JButton sortPhoneBtn = new JButton("Sort by Phone");

        buttonPanel.add(saveBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(sortNameBtn);
        buttonPanel.add(sortPhoneBtn);

        // Display area
        displayArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(displayArea);

        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        // Button actions
        saveBtn.addActionListener(e -> saveContact());
        deleteBtn.addActionListener(e -> deleteContact());
        sortNameBtn.addActionListener(e -> displaySortedContacts("name"));
        sortPhoneBtn.addActionListener(e -> displaySortedContacts("phone"));
    }

    private void saveContact() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and phone are required!");
            return;
        }

        Contact contact = new Contact(name, phone, email);

        if (DatabaseHelper.saveContact(contact)) {
            JOptionPane.showMessageDialog(this, "Contact saved.");
            appendToFile(contact);
        } else {
            JOptionPane.showMessageDialog(this, "Error: duplicate phone or save failed.");
        }
    }

    private void deleteContact() {
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter phone number to delete.");
            return;
        }
        if (DatabaseHelper.deleteContact(phone)) {
            JOptionPane.showMessageDialog(this, "Contact deleted.");
        } else {
            JOptionPane.showMessageDialog(this, "Contact not found.");
        }
    }

    private void displaySortedContacts(String column) {
        try {
            ResultSet rs = DatabaseHelper.getAllContactsSorted(column);
            displayArea.setText("");
            int count = 0;

            do {
                while (rs.next()) {
                    count++;
                    displayArea.append(count + ". " +
                        rs.getString("name") + " | " +
                        rs.getString("phone") + " | " +
                        rs.getString("email") + "\n");
                }
            } while (false); // Demonstrates do-while loop
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void appendToFile(Contact contact) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.txt", true))) {
            writer.write(contact.toString());
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to write to file.");
        }
    }

    public static void main(String[] args) {
        DatabaseHelper.initDatabase();
        SwingUtilities.invokeLater(() -> new ContactManager().setVisible(true));
    }
}
