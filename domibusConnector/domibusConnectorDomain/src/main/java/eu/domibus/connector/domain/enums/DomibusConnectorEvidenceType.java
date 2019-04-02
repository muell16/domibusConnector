package eu.domibus.connector.domain.enums;

public enum DomibusConnectorEvidenceType {
    SUBMISSION_ACCEPTANCE(1),
	SUBMISSION_REJECTION(2),
    RELAY_REMMD_ACCEPTANCE(3),
    RELAY_REMMD_REJECTION(5),
    RELAY_REMMD_FAILURE(4),
    DELIVERY(6),
    NON_DELIVERY(7),
    RETRIEVAL(8),
    NON_RETRIEVAL(9);

	private final int priority;
	
	DomibusConnectorEvidenceType(int priority){
		this.priority=priority;
	}

	public int getPriority() {
		return priority;
	}

	public String toString() {
	    return this.name();
    }
}
