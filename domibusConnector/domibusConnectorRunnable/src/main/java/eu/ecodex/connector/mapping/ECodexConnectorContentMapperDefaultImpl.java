package eu.ecodex.connector.mapping;

import eu.ecodex.connector.common.exception.ImplementationMissingException;
import eu.ecodex.connector.common.message.Message;
import eu.ecodex.connector.common.message.MessageContent;
import eu.ecodex.connector.mapping.ECodexConnectorContentMapper;
import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;

/**
 * This is a very simple example for the implementation of the interface
 * {@link ECodexConnectorContentMapper}.
 * 
 * @author riederb
 * 
 */
public class ECodexConnectorContentMapperDefaultImpl implements ECodexConnectorContentMapper {

    /**
     * This implementation of the interface method
     * {@link ECodexConnectorContentMapper#mapInternationalToNational(Message)}
     * only copies the contents of {@link MessageContent#getECodexContent()} and
     * stores them into {@link MessageContent#setNationalXmlContent(byte[])}.
     * 
     */
    @Override
    public void mapInternationalToNational(Message message) throws ECodexConnectorContentMapperException,
            ImplementationMissingException {
        MessageContent messageContent = message.getMessageContent();
        messageContent.setNationalXmlContent(messageContent.getECodexContent());
    }

    /**
     * This implementation of the interface method
     * {@link ECodexConnectorContentMapper#mapNationalToInternational(Message)}
     * only copies the contents of
     * {@link MessageContent#getNationalXmlContent()} and stores them into
     * {@link MessageContent#setECodexContent(byte[])}.
     * 
     */
    @Override
    public void mapNationalToInternational(Message message) throws ECodexConnectorContentMapperException,
            ImplementationMissingException {
        MessageContent messageContent = message.getMessageContent();
        messageContent.setECodexContent(messageContent.getNationalXmlContent());
    }

}
