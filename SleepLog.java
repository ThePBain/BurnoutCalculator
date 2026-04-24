public class SleepLog {
    private int dayNumber;
    private double hoursSlept;

    public SleepLog(int dayNumber, double hoursSlept) {
        if (dayNumber < 1) {
            throw new IllegalArgumentException("Day number must be 1 or greater.");
        }
        if (hoursSlept < 0 || hoursSlept > 24) {
            throw new IllegalArgumentException("Hours slept must be between 0 and 24.");
        }
        this.dayNumber = dayNumber;
        this.hoursSlept = hoursSlept;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public double getHoursSlept() {
        return hoursSlept;
    }

    public String toString() {
        return "Day " + dayNumber + ": " + hoursSlept + " hours";
    }
}
