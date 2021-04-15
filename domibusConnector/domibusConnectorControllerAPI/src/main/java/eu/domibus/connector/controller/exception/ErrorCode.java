package eu.domibus.connector.controller.exception;

public class ErrorCode {

    public static ErrorCode EVIDENCE_IGNORED_DUE_HIGHER_PRIORITY = new ErrorCode("E103", "The processed evidence is not relevant due another evidence with higher priority");
    public static ErrorCode EVIDENCE_IGNORED_MESSAGE_ALREADY_REJECTED = new ErrorCode("E101", "The processed evidence is ignored, because the business message is already in rejected state");
    public static ErrorCode EVIDENCE_IGNORED_MESSAGE_ALREADY_CONFIRMED = new ErrorCode("E102", "The processed evidence is ignored, because the business message is already in confirmed state");

    private final String errorCode;
    private final String description;

    public ErrorCode(String errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return this.errorCode + ": " + this.description;
    }
}
