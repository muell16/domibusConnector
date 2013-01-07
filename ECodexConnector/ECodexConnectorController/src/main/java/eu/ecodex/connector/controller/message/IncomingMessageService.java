package eu.ecodex.connector.controller.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ecodex.connector.common.enums.ActionEnum;
import eu.ecodex.connector.common.enums.ECodexEvidenceType;
import eu.ecodex.connector.common.enums.ECodexMessageDirection;
import eu.ecodex.connector.common.enums.PartnerEnum;
import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.exception.PersistenceException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageConfirmation;
import eu.ecodex.connector.common.message.MessageDetails;
import eu.ecodex.connector.controller.exception.ECodexConnectorControllerException;
import eu.ecodex.connector.evidences.exception.ECodexConnectorEvidencesToolkitException;
import eu.ecodex.connector.gwc.exception.ECodexConnectorGatewayWebserviceClientException;
import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;
import eu.ecodex.connector.nbc.exception.ECodexConnectorNationalBackendClientException;

public class IncomingMessageService extends AbstractMessageService implements MessageService {

    static Logger LOGGER = LoggerFactory.getLogger(IncomingMessageService.class);

    @Override
    public void handleMessage(Message message) throws ECodexConnectorControllerException {

        try {
            persistenceService.persistMessageIntoDatabase(message, ECodexMessageDirection.GW_TO_NAT);
        } catch (PersistenceException e1) {
            throw new ECodexConnectorControllerException(e1);
        }

        if (connectorProperties.isUseSecurityToolkit()) {
            // TODO: Integration of SecurityToolkit to unpack ASIC-S container
            // and TrustOKToken
            LOGGER.warn("SecurityToolkit not available yet! Must send message unsecure!");
        }

        if (connectorProperties.isUseEvidencesToolkit()) {
            createRelayREMMDEvidenceAndSendIt(message);
        }

        if (connectorProperties.isUseContentMapper()) {
            try {
                contentMapper.mapInternationalToNational(message.getMessageContent());
            } catch (ECodexConnectorContentMapperException e) {
                throw new ECodexConnectorControllerException("Error mapping content of message into national format!",
                        e);
            } catch (ImplementationMissingException e) {
                throw new ECodexConnectorControllerException(e);
            }
        }

        try {
            nationalBackendClient.deliverMessage(message);
        } catch (ECodexConnectorNationalBackendClientException e) {
            throw new ECodexConnectorControllerException("Error delivering message to national backend client!", e);
        } catch (ImplementationMissingException e) {
            throw new ECodexConnectorControllerException(e);
        }

    }

    private void createRelayREMMDEvidenceAndSendIt(Message originalMessage) throws ECodexConnectorControllerException {
        MessageConfirmation relayRemMDAcceptance = null;
        try {
            relayRemMDAcceptance = evidencesToolkit.createRelayREMMDAcceptance(originalMessage);
            originalMessage.addConfirmation(relayRemMDAcceptance);
            persistenceService.persistEvidenceForMessageIntoDatabase(originalMessage,
                    relayRemMDAcceptance.getEvidence(), ECodexEvidenceType.RELAY_REMMD_ACCEPTANCE);
        } catch (ECodexConnectorEvidencesToolkitException e) {
            throw new ECodexConnectorControllerException("Error creating RelayREMMDAcceptance for message!", e);
        }

        MessageDetails details = new MessageDetails();
        details.setRefToMessageId(originalMessage.getMessageDetails().getEbmsMessageId());
        details.setConversationId(originalMessage.getMessageDetails().getConversationId());
        details.setService(originalMessage.getMessageDetails().getService());
        details.setAction(ActionEnum.Evidence_RelayREMMD);
        PartnerEnum fromPartner = PartnerEnum.findValue(connectorProperties.getGatewayName(),
                connectorProperties.getGatewayRole());
        details.setFromPartner(fromPartner);
        details.setToPartner(originalMessage.getMessageDetails().getFromPartner());

        Message evidenceMessage = new Message(details, relayRemMDAcceptance);

        try {
            gatewayWebserviceClient.sendMessage(evidenceMessage);
        } catch (ECodexConnectorGatewayWebserviceClientException e) {
            LOGGER.error("Exception sending RelayREMMD evidence back to sender gateway of message "
                    + originalMessage.getMessageDetails().getEbmsMessageId(), e);
        }
    }

}
