package eu.ecodex.connector.mapping;

import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;

public class NationalContentMapper implements ECodexConnectorContentMapper {

    @Override
    public void mapInternationalToNational(Message message) throws ECodexConnectorContentMapperException,
            ImplementationMissingException {
        MessageContent messageContent = message.getMessageContent();
        messageContent.setNationalXmlContent(messageContent.getECodexContent());
    }

    @Override
    public void mapNationalToInternational(Message message) throws ECodexConnectorContentMapperException,
            ImplementationMissingException {
        MessageContent messageContent = message.getMessageContent();
        messageContent.setECodexContent(messageContent.getNationalXmlContent());
    }

}
