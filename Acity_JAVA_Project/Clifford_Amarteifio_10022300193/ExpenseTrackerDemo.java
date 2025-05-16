import java.time.LocalDate;
import java.util.Map;

public class ExpenseTrackerDemo {
    public static void main(String[] args) {
        // Create an instance of ExpenseManager
        ExpenseManager manager = new ExpenseManager();

        // Add some sample expenses
        manager.addExpense(25.50, ExpenseCategory.FOOD, "Lunch at restaurant");
        manager.addExpense(35.00, ExpenseCategory.TRANSPORT, "Taxi ride");
        manager.addExpense(50.00, ExpenseCategory.ENTERTAINMENT, "Movie tickets");

        // Get current year and month
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        // Display monthly expenses
        System.out.println("\nMonthly Expenses for " + currentMonth + "/" + currentYear);
        System.out.println("Total: $" + String.format("%.2f", manager.getMonthlyExpenses(currentYear, currentMonth)));

        // Display expenses by category
        System.out.println("\nExpenses by Category:");
        Map<ExpenseCategory, Double> expensesByCategory = manager.getMonthlyExpensesByCategory(currentYear, currentMonth);
        for (Map.Entry<ExpenseCategory, Double> entry : expensesByCategory.entrySet()) {
            System.out.printf("%s: $%.2f%n", entry.getKey(), entry.getValue());
        }

        // Display yearly expenses
        System.out.println("\nYearly Expenses for " + currentYear);
        System.out.println("Total: $" + String.format("%.2f", manager.getYearlyExpenses(currentYear)));

        // Display all expenses
        System.out.println("\nAll Expenses:");
        for (Expense expense : manager.getAllExpenses()) {
            System.out.println(expense);
        }
    }
} 