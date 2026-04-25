import java.util.ArrayList;

public class BurnoutCalculator {
    public static final double MAX_WORKLOAD = 40.0;
    public static final double MAX_SLEEP = 25.0;
    public static final double MAX_DENSITY = 20.0;
    public static final double MAX_RECOVERY = 15.0;

    private Student student;
    private double workloadScore;
    private double sleepScore;
    private double densityScore;
    private double recoveryScore;
    private double riskScore;
    private RiskTier riskTier;

    public BurnoutCalculator(Student student) {
        this.student = student;
        this.riskScore = 0.0;
        this.riskTier = RiskTier.LOW;
    }

    public void calculateRisk() {
        workloadScore = calculateWorkload();
        sleepScore = calculateSleepPenalty();
        densityScore = calculateDensity();
        recoveryScore = calculateRecoveryPenalty();

        riskScore = workloadScore + sleepScore + densityScore + recoveryScore;
        if (riskScore > 100) {
            riskScore = 100;
        }

        riskTier = RiskTier.fromScore(riskScore);
    }

    private double calculateWorkload() {
        ArrayList<Assignment> list = student.getAssignments();
        double raw = 0;
        for (int i = 0; i < list.size(); i++) {
            Assignment a = list.get(i);
            double urgency;
            if (a.getDaysUntilDue() <= 1) {
                urgency = 10;
            } else if (a.getDaysUntilDue() <= 3) {
                urgency = 7;
            } else if (a.getDaysUntilDue() <= 7) {
                urgency = 4;
            } else {
                urgency = 1;
            }
            raw = raw + (urgency * a.getWeight());
        }
        double scaled = raw * 0.35;
        if (scaled > MAX_WORKLOAD) {
            scaled = MAX_WORKLOAD;
        }
        return scaled;
    }

    private double calculateSleepPenalty() {
        ArrayList<SleepLog> logs = student.getSleepLogs();
        if (logs.size() == 0) {
            return 0;
        }

        double sum = 0;
        for (int i = 0; i < logs.size(); i++) {
            sum = sum + logs.get(i).getHoursSlept();
        }
        double avg = sum / logs.size();

        // each hour below the 8h target costs 6.25 points, so 4h avg maxes out the meter
        double penalty = (8.0 - avg) * 6.25;
        if (penalty < 0) penalty = 0;
        if (penalty > MAX_SLEEP) penalty = MAX_SLEEP;
        return penalty;
    }

    // deadline clustering: how many assignments are due within 3 days
    private double calculateDensity() {
        ArrayList<Assignment> list = student.getAssignments();
        int close = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDaysUntilDue() <= 3) {
                close = close + 1;
            }
        }
        if (close >= 4) {
            return MAX_DENSITY;
        } else if (close == 3) {
            return 14;
        } else if (close == 2) {
            return 8;
        } else {
            return 0;
        }
    }

    private double calculateRecoveryPenalty() {
        ArrayList<SleepLog> logs = student.getSleepLogs();
        if (logs.isEmpty()) {
            return 0;
        }

        int maxStreak = 0;
        int currentStreak = 0;
        for (int i = 0; i < logs.size(); i++) {
            if (logs.get(i).getHoursSlept() < 7.0) {
                currentStreak++;
                if (currentStreak > maxStreak) {
                    maxStreak = currentStreak;
                }
            } else {
                currentStreak = 0;
            }
        }

        double penalty = maxStreak * 3.75;
        if (penalty > MAX_RECOVERY) penalty = MAX_RECOVERY;
        return penalty;
    }

    public String getTopDriver() {
        double wRatio = workloadScore / MAX_WORKLOAD;
        double sRatio = sleepScore / MAX_SLEEP;
        double dRatio = densityScore / MAX_DENSITY;
        double rRatio = recoveryScore / MAX_RECOVERY;

        double best = wRatio;
        String name = "Workload";
        if (sRatio > best) { best = sRatio; name = "Sleep"; }
        if (dRatio > best) { best = dRatio; name = "Deadline density"; }
        if (rRatio > best) { best = rRatio; name = "Lack of recovery"; }

        if (best == 0) {
            return "None";
        }
        return name;
    }

    public Student getStudent() { return student; }
    public double getWorkloadScore() { return workloadScore; }
    public double getSleepScore() { return sleepScore; }
    public double getDensityScore() { return densityScore; }
    public double getRecoveryScore() { return recoveryScore; }
    public double getRiskScore() { return riskScore; }
    public RiskTier getRiskTier() { return riskTier; }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Burnout Risk Report for ").append(student.getName()).append(" ---\n");
        sb.append("Assignments tracked: ").append(student.getAssignments().size()).append("\n");
        sb.append("Sleep logs tracked:  ").append(student.getSleepLogs().size()).append("\n\n");

        sb.append("Component breakdown:\n");
        sb.append(formatComponent("Workload", workloadScore, MAX_WORKLOAD));
        sb.append(formatComponent("Sleep",    sleepScore,    MAX_SLEEP));
        sb.append(formatComponent("Density",  densityScore,  MAX_DENSITY));
        sb.append(formatComponent("Recovery", recoveryScore, MAX_RECOVERY));
        sb.append("\n");

        sb.append(String.format("Risk Score: %.1f / 100  %s%n", riskScore, scoreBar(riskScore, 100)));
        sb.append("Risk Tier:  ").append(riskTier.displayName()).append("\n");
        sb.append("Top driver: ").append(getTopDriver()).append("\n");

        RecommendationEngine engine = new RecommendationEngine(this);
        sb.append("\nRecommendation: ").append(engine.generateRecommendation()).append("\n");
        return sb.toString();
    }

    private String formatComponent(String name, double score, double max) {
        return String.format("  %-9s %4.1f / %-4.1f  %s%n", name, score, max, scoreBar(score, max));
    }

    private String scoreBar(double score, double max) {
        int width = 20;
        int filled = (int) Math.round((score / max) * width);
        if (filled > width) filled = width;
        if (filled < 0) filled = 0;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            sb.append(i < filled ? "#" : "-");
        }
        sb.append("]");
        return sb.toString();
    }
}
