public class Assignment {
    private String title;
    private int daysUntilDue;
    private int weight; // rough difficulty or size rating, 1 to 5

    public Assignment(String title, int daysUntilDue, int weight) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (daysUntilDue < 0) {
            throw new IllegalArgumentException("Days until due cannot be negative.");
        }
        if (weight < 1 || weight > 5) {
            throw new IllegalArgumentException("Weight must be between 1 and 5.");
        }
        this.title = title.trim();
        this.daysUntilDue = daysUntilDue;
        this.weight = weight;
    }

    public String getTitle() {
        return title;
    }

    public int getDaysUntilDue() {
        return daysUntilDue;
    }

    public int getWeight() {
        return weight;
    }

    public String toString() {
        return title + " (due in " + daysUntilDue + " days, weight " + weight + ")";
    }
}
