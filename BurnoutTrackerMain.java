import java.util.Scanner;

public class BurnoutTrackerMain {

    public static void main(String[] args) {
        // run "java BurnoutTrackerMain --demo" to skip prompts and load sample data,
        // which is handy for the presentation.
        if (args.length > 0 && args[0].equals("--demo")) {
            runDemo();
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Burnout Risk Tracker ===");
        String name = InputHelper.readNonEmptyLine(scanner, "Enter your name: ");
        Student student = new Student(name);

        int numAssignments = InputHelper.readIntInRange(scanner,
            "How many assignments do you want to add? ", 0, 50);
        addAssignments(scanner, student, numAssignments);

        int numSleep = InputHelper.readIntInRange(scanner,
            "\nHow many days of sleep do you want to log? ", 0, 30);
        addSleepLogs(scanner, student, numSleep);

        runReport(student);

        menuLoop(scanner, student);

        scanner.close();
        System.out.println("Goodbye.");
    }

    // keeps the program going so the user can add more data and rerun the report
    private static void menuLoop(Scanner scanner, Student student) {
        while (true) {
            System.out.println("\nWhat would you like to do next?");
            System.out.println("  1) Add more assignments");
            System.out.println("  2) Add more sleep logs");
            System.out.println("  3) Recalculate and show report");
            System.out.println("  4) View current data");
            System.out.println("  5) Quit");

            int choice = InputHelper.readIntInRange(scanner, "Choose 1-5: ", 1, 5);
            switch (choice) {
                case 1:
                    int moreA = InputHelper.readIntInRange(scanner,
                        "How many more assignments? ", 1, 50);
                    addAssignments(scanner, student, moreA);
                    break;
                case 2:
                    int moreS = InputHelper.readIntInRange(scanner,
                        "How many more days of sleep? ", 1, 30);
                    addSleepLogs(scanner, student, moreS);
                    break;
                case 3:
                    runReport(student);
                    break;
                case 4:
                    printCurrentData(student);
                    break;
                case 5:
                    return;
            }
        }
    }

    private static void addAssignments(Scanner scanner, Student student, int count) {
        int startIndex = student.getAssignments().size();
        for (int i = 0; i < count; i++) {
            System.out.println("\nAssignment " + (startIndex + i + 1) + ":");
            String title = InputHelper.readNonEmptyLine(scanner, "  Title: ");
            int days    = InputHelper.readIntInRange(scanner, "  Days until due: ", 0, 365);
            int weight  = InputHelper.readIntInRange(scanner,
                "  Weight (1 = small/easy, 5 = large/hard): ", 1, 5);
            student.addAssignment(new Assignment(title, days, weight));
        }
    }

    private static void addSleepLogs(Scanner scanner, Student student, int count) {
        // continue numbering from whatever day we are already on
        int startDay = student.getSleepLogs().size();
        for (int i = 0; i < count; i++) {
            int dayNumber = startDay + i + 1;
            double hours = InputHelper.readDoubleInRange(scanner,
                "  Day " + dayNumber + " hours slept: ", 0, 24);
            student.addSleepLog(new SleepLog(dayNumber, hours));
        }
    }

    private static void runReport(Student student) {
        BurnoutCalculator calc = new BurnoutCalculator(student);
        calc.calculateRisk();
        System.out.println(calc.generateReport());
    }

    private static void printCurrentData(Student student) {
        System.out.println("\n--- Current data for " + student.getName() + " ---");
        System.out.println("Assignments (" + student.getAssignments().size() + "):");
        if (student.getAssignments().isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (int i = 0; i < student.getAssignments().size(); i++) {
                System.out.println("  " + (i + 1) + ". " + student.getAssignments().get(i));
            }
        }
        System.out.println("Sleep logs (" + student.getSleepLogs().size() + "):");
        if (student.getSleepLogs().isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (int i = 0; i < student.getSleepLogs().size(); i++) {
                System.out.println("  " + student.getSleepLogs().get(i));
            }
        }
    }

    private static void runDemo() {
        System.out.println("=== Burnout Risk Tracker (demo mode) ===");
        Student student = new Student("Demo Student");

        student.addAssignment(new Assignment("SE 2070 Project",    1, 5));
        student.addAssignment(new Assignment("Calc II Homework",   2, 3));
        student.addAssignment(new Assignment("History Essay",      3, 4));
        student.addAssignment(new Assignment("Physics Lab Report", 6, 2));
        student.addAssignment(new Assignment("Reading Quiz",      10, 1));

        double[] sleep = {5.5, 5.0, 6.5, 4.5, 5.5, 7.0, 6.0};
        for (int i = 0; i < sleep.length; i++) {
            student.addSleepLog(new SleepLog(i + 1, sleep[i]));
        }

        runReport(student);
    }
}
