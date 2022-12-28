package eu.domibus.connector.controller.exception;

public class ErrorCode {

    public static final ErrorCode DELIVERY_TIMEOUT_REACHED = new ErrorCode("T101", "The maximum waiting time for a DELIVERY evidence for this business message has been reached!");
    public static final ErrorCode EVIDENCE_IGNORED_MESSAGE_ALREADY_REJECTED = new ErrorCode("E101", "The processed evidence is ignored, because the business message is already in rejected state");
    public static final ErrorCode EVIDENCE_IGNORED_DUE_DUPLICATE = new ErrorCode("E102", "The processed evidence is ignored, because max occurence number of evidence type exceeded");
    public static final ErrorCode EVIDENCE_IGNORED_DUE_HIGHER_PRIORITY = new ErrorCode("E103", "The processed evidence is not relevant due another evidence with higher priority");
    public static final ErrorCode EVIDENCE_IGNORED = new ErrorCode("E104", "Evidence ignored, because it cannot change message state");

    public static final ErrorCode LINK_PARTNER_NOT_FOUND = new ErrorCode("L104", "The requested LinkPartner is not configured");
    public static final ErrorCode LINK_PARTNER_NOT_ACTIVE = new ErrorCode("L101", "The requested LinkPartner is not active");

    public static final ErrorCode DOMAIN_MATCHING_ERROR = new ErrorCode("D101", "The msg could not be associated with a domain because no domain matched!");
    public static final ErrorCode MULTIPLE_DOMAIN_MATCHING_ERROR = new ErrorCode("D102", "The msg could not be associated with a domain because multiple domains will match!");

    public static final ErrorCode P_MODE_ERROR = new ErrorCode("P100", "General P-Mode error");
    public static final ErrorCode P_MODE_SERVICE_NOT_FOUND = new ErrorCode("P101", "The service definition is not sufficient enough");
    public static final ErrorCode P_MODE_SERVICE_ACTION_LEG_NOT_FOUND = new ErrorCode("P102", "The service action combination is not sufficient found no leg");

    public static final ErrorCode OTHER = new ErrorCode("C100", "Unspecified issue");
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
