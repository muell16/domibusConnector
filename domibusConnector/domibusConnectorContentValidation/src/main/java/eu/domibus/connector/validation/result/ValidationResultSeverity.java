package eu.domibus.connector.validation.result;

public enum ValidationResultSeverity {

    INFO(1), WARN(2), ERROR(3), FATAL(4);

    private int severityLevel;

    private ValidationResultSeverity(int level) {
        severityLevel = level;
    }

    public int getSeverityLevel() {
        return severityLevel;
    }
}
