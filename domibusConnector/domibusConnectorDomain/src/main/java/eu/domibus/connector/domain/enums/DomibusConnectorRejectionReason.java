package eu.domibus.connector.domain.enums;


public enum DomibusConnectorRejectionReason {

    SOAP_FAULT(""),
    SAML_TOKEN_VALIDATION(""),
    WS_ADDRESSING_FAULT(""),
    UNKNOWN_ORIGINATOR_ADDRESS(""),
    UNKNOWN_RECIPIENT_ADDRESS(""),
    UNSPECIFIC_PROCESSING_ERROR(""),
    WRONG_INPUT_DATA(""),
    DUPLICATE_MSG_ID(""),
    OTHER(""),
    BACKEND_REJECTION("The connector backend or backend application rejected the message"),
    UNREACHABLE("");

    private final String reasonText;

    DomibusConnectorRejectionReason(String reasonText) {
        this.reasonText = reasonText;
    }

    public String getReasonText() {
        return reasonText;
    }
}
