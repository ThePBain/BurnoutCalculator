public class BurnoutCalculatorTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testLowRiskForHealthyStudent();
        testHighRiskForClusteredHeavyWork();
        testSleepPenaltyBoundaries();
        testRiskScoreCappedAt100();
        testRecoveryPenaltyForConsecutiveBadNights();
        testTopDriverIdentifiesSleep();
        testAssignmentValidatesWeight();
        testSleepLogValidatesHours();

        System.out.println();
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testLowRiskForHealthyStudent() {
        Student s = new Student("Healthy");
        s.addAssignment(new Assignment("Easy reading", 14, 1));
        for (int i = 1; i <= 5; i++) s.addSleepLog(new SleepLog(i, 8.0));

        BurnoutCalculator c = new BurnoutCalculator(s);
        c.calculateRisk();

        check("Healthy student is LOW", c.getRiskTier() == RiskTier.LOW);
        check("Healthy student score < 30", c.getRiskScore() < 30);
    }

    private static void testHighRiskForClusteredHeavyWork() {
        Student s = new Student("Stressed");
        s.addAssignment(new Assignment("Big project",    1, 5));
        s.addAssignment(new Assignment("Heavy essay",    2, 5));
        s.addAssignment(new Assignment("Final exam",     3, 5));
        s.addAssignment(new Assignment("Lab report",     3, 4));
        for (int i = 1; i <= 5; i++) s.addSleepLog(new SleepLog(i, 5.0));

        BurnoutCalculator c = new BurnoutCalculator(s);
        c.calculateRisk();

        check("Stressed student is HIGH", c.getRiskTier() == RiskTier.HIGH);
    }

    private static void testSleepPenaltyBoundaries() {
        // sleep penalty is linear: (8 - avg) * 6.25, clamped to [0, 25]
        check("avg 8.0 sleep gives 0 penalty",      approxEq(sleepScoreFor(8.0), 0));
        check("avg 7.0 sleep gives ~6.25 penalty",  approxEq(sleepScoreFor(7.0), 6.25));
        check("avg 6.0 sleep gives ~12.5 penalty",  approxEq(sleepScoreFor(6.0), 12.5));
        check("avg 4.0 sleep hits 25 cap",          approxEq(sleepScoreFor(4.0), 25));
        check("avg 9.0 sleep still clamps to 0",    approxEq(sleepScoreFor(9.0), 0));
    }

    private static boolean approxEq(double a, double b) {
        return Math.abs(a - b) < 0.01;
    }

    private static double sleepScoreFor(double hours) {
        Student s = new Student("X");
        for (int i = 1; i <= 5; i++) s.addSleepLog(new SleepLog(i, hours));
        BurnoutCalculator c = new BurnoutCalculator(s);
        c.calculateRisk();
        return c.getSleepScore();
    }

    private static void testRiskScoreCappedAt100() {
        Student s = new Student("MaxStress");
        for (int i = 0; i < 10; i++) {
            s.addAssignment(new Assignment("A" + i, 0, 5));
        }
        for (int i = 1; i <= 7; i++) s.addSleepLog(new SleepLog(i, 2.0));

        BurnoutCalculator c = new BurnoutCalculator(s);
        c.calculateRisk();

        check("Risk score never exceeds 100", c.getRiskScore() <= 100.0);
    }

    private static void testRecoveryPenaltyForConsecutiveBadNights() {
        Student s = new Student("NoRecovery");
        double[] hours = {4.0, 4.0, 4.0, 4.0, 8.0};
        for (int i = 0; i < hours.length; i++) s.addSleepLog(new SleepLog(i + 1, hours[i]));

        BurnoutCalculator c = new BurnoutCalculator(s);
        c.calculateRisk();

        check("4+ night streak hits max recovery penalty",
            c.getRecoveryScore() == BurnoutCalculator.MAX_RECOVERY);
    }

    private static void testTopDriverIdentifiesSleep() {
        Student s = new Student("Sleepy");
        s.addAssignment(new Assignment("One thing", 14, 1));
        for (int i = 1; i <= 5; i++) s.addSleepLog(new SleepLog(i, 4.0));

        BurnoutCalculator c = new BurnoutCalculator(s);
        c.calculateRisk();

        String driver = c.getTopDriver();
        check("Top driver is Sleep or Lack of recovery",
            driver.equals("Sleep") || driver.equals("Lack of recovery"));
    }

    private static void testAssignmentValidatesWeight() {
        boolean threw = false;
        try {
            new Assignment("Bad", 3, 99);
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("Assignment rejects weight > 5", threw);
    }

    private static void testSleepLogValidatesHours() {
        boolean threw = false;
        try {
            new SleepLog(1, 30);
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("SleepLog rejects hours > 24", threw);
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }
}
