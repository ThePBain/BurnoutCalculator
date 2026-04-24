public enum RiskTier {
    LOW, MODERATE, HIGH;

    public String displayName() {
        String s = name();
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

    public static RiskTier fromScore(double score) {
        if (score < 30) {
            return LOW;
        } else if (score < 60) {
            return MODERATE;
        } else {
            return HIGH;
        }
    }
}
