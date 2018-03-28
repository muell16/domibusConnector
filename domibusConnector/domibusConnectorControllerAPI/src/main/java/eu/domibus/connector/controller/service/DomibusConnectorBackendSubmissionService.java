
package eu.domibus.connector.controller.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;

/**
 * should be implemented by the Service which is delivering the messages
 * to the backend
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public interface DomibusConnectorBackendSubmissionService {

    /**
     * submit a persisted message for further processing to controller
     * @param message - the message
     */
    public void submitToController(DomibusConnectorMessage message);

    /**
     * set message as delivered to national system
     *  *) create evidence and send it (DeliveryEvidence)
     *
     * @param message - the message
     *
     */
    public DomibusConnectorMessage prepareMessageForNationalDelivery(DomibusConnectorMessage message);


}
