public class RecommendationEngine {
    private BurnoutCalculator calc;

    public RecommendationEngine(BurnoutCalculator calc) {
        this.calc = calc;
    }

    public String generateRecommendation() {
        RiskTier tier = calc.getRiskTier();
        String driver = calc.getTopDriver();

        if (tier == RiskTier.LOW) {
            return "You are doing well. Keep up the steady pace.";
        }

        if (tier == RiskTier.MODERATE) {
            return "Watch your workload. " + adviceFor(driver)
                + " Try to spread out your work before things pile up.";
        }

        return "High risk of burnout. " + adviceFor(driver)
            + " Consider talking to a teacher or pushing a deadline if possible.";
    }

    private String adviceFor(String driver) {
        if (driver.equals("Sleep")) {
            return "Your average sleep is the main driver. Protect at least 7 hours per night.";
        }
        if (driver.equals("Lack of recovery")) {
            return "You have a streak of short nights. Plan a full recovery night soon.";
        }
        if (driver.equals("Deadline density")) {
            return "Deadlines are clustered. Start the earliest-due items today to break up the pile.";
        }
        if (driver.equals("Workload")) {
            return "Your overall workload is heavy. Rank assignments by weight and tackle the biggest first.";
        }
        return "Review your week and identify one thing you can push or shorten.";
    }
}
