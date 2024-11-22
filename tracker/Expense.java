package tracker;
import java.sql.Date;

public class Expense {
    private String description;
    private double amount;
    private String category;  // New field for category
    private Date date;

    public Expense(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category; // Initialize category
        this.date = new Date(0);
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;  // Getter for category
    }

    public Date getDate() {
        return date;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
