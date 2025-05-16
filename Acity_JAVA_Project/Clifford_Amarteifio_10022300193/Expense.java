import java.time.LocalDate;

public class Expense {
    private double amount;
    private ExpenseCategory category;
    private String description;
    private LocalDate date;

    // Constructor
    public Expense(double amount, ExpenseCategory category, String description, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    // Getters
    public double getAmount() {
        return amount;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("Date: %s, Amount: $%.2f, Category: %s, Description: %s",
                date, amount, category, description);
    }

    // Method to convert expense to CSV format for file storage
    public String toCsvString() {
        return String.format("%s,%f,%s,%s", date, amount, category, description);
    }
} 