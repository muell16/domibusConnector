package eu.domibus.connector.controller.service;


import eu.domibus.connector.controller.exception.DomibusConnectorRejectSubmissionException;

/**
 * This service provides the ability to reject the submission to the
 * gateway by the backendLinkModule.
 * This service should be called on any point of submitting a message
 * to the gateway, if the system is unable to submit the message to the gateway.
 * <p>
 * The DomibusConnectorSubmissionRejectionService has to ensure that for the
 * message
 *
 * <ul>
 * <li>if the message is a business_message a SUBMISSION_REJECTION is generated and submitted to the submitting connectorBackend</li>
 * <li>if the mssage is just an ConfirmationMessage/EvidenceMessage no evidence/confirmation is generated.</li>
 * </ul>
 *
 */
public interface DomibusConnectorSubmissionRejectionService {

    public void rejectSubmission(DomibusConnectorRejectSubmissionException cause);

}
