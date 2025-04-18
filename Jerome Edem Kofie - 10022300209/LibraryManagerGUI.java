import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class LibraryManagerGUI extends JFrame {
    private ArrayList<Book> books = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> bookList = new JList<>(listModel);
    private JTextField titleField = new JTextField(15);
    private JTextField authorField = new JTextField(15);
    private JTextField isbnField = new JTextField(15);
    private File dataFile = new File("books.dat");

    public LibraryManagerGUI() {
        setTitle("Library Manager");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window on screen

        loadBooks();  // Load existing books from file

        // Layout and design setup
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 37, 41));  // Dark header background
        JLabel headerLabel = new JLabel("Library Management System");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Input panel for adding book details
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240));

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("ISBN:"));
        inputPanel.add(isbnField);

        JButton addBtn = new JButton("Add Book");
        JButton deleteBtn = new JButton("Delete Book");
        JButton searchBtn = new JButton("Search Book");
        JButton updateBtn = new JButton("Update Book");

        styleButton(addBtn);
        styleButton(deleteBtn);
        styleButton(searchBtn);
        styleButton(updateBtn);

        inputPanel.add(addBtn);
        inputPanel.add(deleteBtn);
        inputPanel.add(searchBtn);
        inputPanel.add(updateBtn);

        add(inputPanel, BorderLayout.WEST);

        // Book list panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(bookList);
        listPanel.add(scrollPane, BorderLayout.CENTER);
        add(listPanel, BorderLayout.CENTER);

        // Footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(33, 37, 41));
        JLabel footerLabel = new JLabel("Manage your books easily!");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        // Button actions
        addBtn.addActionListener(e -> addBook());
        deleteBtn.addActionListener(e -> deleteBook());
        searchBtn.addActionListener(e -> searchBook());
        updateBtn.addActionListener(e -> updateBook());

        refreshList();
        setVisible(true);
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();
 
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        // Check for duplicate ISBN
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                JOptionPane.showMessageDialog(this, "Book with this ISBN already exists!");
                return;
            }
        }

        books.add(new Book(title, author, isbn));
        saveBooks();
        refreshList();
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
    }

    private void deleteBook() {
        int selected = bookList.getSelectedIndex();
        if (selected >= 0) {
            books.remove(selected);
            saveBooks();
            refreshList();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
        }
    }

    private void searchBook() {
        String keyword = JOptionPane.showInputDialog(this, "Enter title, author, or ISBN to search:");
        if (keyword == null || keyword.trim().isEmpty()) return;

        StringBuilder result = new StringBuilder();
        for (Book b : books) {
            if (b.getTitle().contains(keyword) || b.getAuthor().contains(keyword) || b.getIsbn().contains(keyword)) {
                result.append(b).append("\n");
            }
        }

        if (result.length() == 0)
            JOptionPane.showMessageDialog(this, "No matches found!");
        else
            JOptionPane.showMessageDialog(this, result.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateBook() {
        int selected = bookList.getSelectedIndex();
        if (selected >= 0) {
            Book book = books.get(selected);
            String newTitle = JOptionPane.showInputDialog(this, "Enter new title (current: " + book.getTitle() + "):");
            String newAuthor = JOptionPane.showInputDialog(this, "Enter new author (current: " + book.getAuthor() + "):");
            String newIsbn = JOptionPane.showInputDialog(this, "Enter new ISBN (current: " + book.getIsbn() + "):");

            if (newTitle != null && newAuthor != null && newIsbn != null) {
                book.setTitle(newTitle);
                book.setAuthor(newAuthor);
                book.setIsbn(newIsbn);
                saveBooks();
                refreshList();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to update.");
        }
    }

    private void refreshList() {
        listModel.clear();
        for (Book b : books) {
            listModel.addElement(b.toString());
        }
    }

    private void loadBooks() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            books = (ArrayList<Book>) in.readObject();
        } catch (Exception e) {
            books = new ArrayList<>();
        }
    }

    private void saveBooks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(0, 123, 255));  // Blue color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public static void main(String[] args) {
        new LibraryManagerGUI();
    }
}
