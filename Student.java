import java.util.ArrayList;

public class Student {
    private String name;
    private ArrayList<Assignment> assignments;
    private ArrayList<SleepLog> sleepLogs;

    public Student(String name) {
        this.name = name;
        this.assignments = new ArrayList<Assignment>();
        this.sleepLogs = new ArrayList<SleepLog>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public ArrayList<SleepLog> getSleepLogs() {
        return sleepLogs;
    }

    public void addAssignment(Assignment a) {
        assignments.add(a);
    }

    public void addSleepLog(SleepLog s) {
        sleepLogs.add(s);
    }
}