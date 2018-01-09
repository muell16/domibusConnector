package eu.domibus.connector.persistence.service.impl.helper;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMsgContDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont;
import eu.domibus.connector.persistence.service.PersistenceException;
import eu.domibus.connector.persistence.service.impl.loadstore.StoreType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class MsgContentPersistenceService {

//    static interface LoadFromMsgContHandler<T> {
//        default public int loadFromPriority() {
//            return 100;
//        }
//        
//        public T loadFromMsgCont(PDomibusConnectorMsgCont msgCont);
//        
//        public boolean canLoadFrom(PDomibusConnectorMsgCont msgCont);
//        
//    }
    
    private final static Logger LOGGER = LoggerFactory.getLogger(MsgContentPersistenceService.class);
    
    @Autowired
    private DomibusConnectorMsgContDao msgContDao;
    
    @Autowired
    private DomibusConnectorMessageDao msgDao;
    
    /**
     * TODO!
     * @param messageBuilder
     * @param dbMessage
     * @throws PersistenceException 
     */
    public void loadMsgContent(final @Nonnull DomibusConnectorMessageBuilder messageBuilder, final PDomibusConnectorMessage dbMessage) throws PersistenceException {
        List<PDomibusConnectorMsgCont> findByMessage = this.msgContDao.findByMessage(dbMessage);
        //map content back
        Optional<PDomibusConnectorMsgCont> findFirst = findByMessage.stream()
                .filter( c -> StoreType.MESSAGE_CONTENT.getDbString().equals(c.getContentType()))
                .findFirst();
        if (findFirst.isPresent()) {
            DomibusConnectorMessageContent messageContent = mapFromMsgCont(findFirst.get(), DomibusConnectorMessageContent.class);
            messageBuilder.setMessageContent(messageContent);
        }
        //map evidence back
        findByMessage.stream()
                .filter( c -> StoreType.MESSAGE_CONFIRMATION.getDbString().equals(c.getContentType()))
                .forEach( c -> {
                    DomibusConnectorMessageConfirmation msgConfirmation = mapFromMsgCont(c, DomibusConnectorMessageConfirmation.class);
                    messageBuilder.addConfirmation(msgConfirmation);
                });
        //map attachments back
        findByMessage.stream()
                .filter( c -> StoreType.MESSAGE_ATTACHMENT.getDbString().equals(c.getContentType()))
                .forEach( c -> {
                    DomibusConnectorMessageAttachment msgAttachment = mapFromMsgCont(c, DomibusConnectorMessageAttachment.class);
                    messageBuilder.addAttachment(msgAttachment);
                });        
    }
    
    /**
     * takes a message and stores all content into the database
     * deletes all old content regarding the message and persists it again in the database
     * 
     * @param message the message
     */
    public void storeMsgContent(@Nonnull DomibusConnectorMessage message) throws PersistenceException {
        //handle document
        List<PDomibusConnectorMsgCont> toStoreList = new ArrayList<>();
        DomibusConnectorMessageContent messageContent = message.getMessageContent();
        if (messageContent != null) {
            toStoreList.add(mapToMsgCont(StoreType.MESSAGE_CONTENT, messageContent));
        }
        //handle attachments
        for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
            toStoreList.add(mapToMsgCont(StoreType.MESSAGE_ATTACHMENT, attachment));
        }
        //handle confirmations
        for (DomibusConnectorMessageConfirmation c : message.getMessageConfirmations()) {
            toStoreList.add(mapToMsgCont(StoreType.MESSAGE_CONFIRMATION, c));            
        }
        PDomibusConnectorMessage findMessageByMessage = this.msgDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        this.msgContDao.deleteByMessage(findMessageByMessage);   //delete old contents
        this.msgContDao.save(toStoreList); //save new contents
    }        
    
    /**
     * Takes a StoreType and a Object
     * maps that object into @see eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont
     * 
     * 
     */
    PDomibusConnectorMsgCont mapToMsgCont(            
            @Nonnull StoreType type, 
            @Nonnull Object object) throws PersistenceException {        
        try {
            PDomibusConnectorMsgCont msgCont = new PDomibusConnectorMsgCont();
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(object);
            byte[] toByteArray = byteOut.toByteArray();
            String md5DigestAsHex = DigestUtils.md5DigestAsHex(toByteArray);
            msgCont.setContentType(type.getDbString());
            msgCont.setChecksum(md5DigestAsHex);
            msgCont.setContent(toByteArray);
            return msgCont;
        } catch (IOException ioe) {
            String error = String.format("storeMsgContent: A error occured during serializing [%s] object [%s] and storing it into database", type, object);
            LOGGER.error(error, ioe);
            throw new PersistenceException(error);
        }        
    }
    
    <T> T mapFromMsgCont(@Nonnull PDomibusConnectorMsgCont msgContent, Class<T> clazz) throws PersistenceException {
        ObjectInputStream inputStream;
        try {
            byte[] byteContent = msgContent.getContent();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteContent);
            inputStream = new ObjectInputStream(byteInputStream);
            T confirmation = (T) inputStream.readObject();                                    
            return confirmation;            
        } catch (IOException ex) {
            String error = String.format("mapFromMsgCont: IOException occured during reading object out of message content [%s]", msgContent);
            LOGGER.error(error);
            throw new PersistenceException(error, ex);
        } catch (ClassNotFoundException ex) {
            String error = String.format("mapFromMsgCont: Class not found exception occured during reading object out of message content [%s], "
                    + "maybe incompatible updates or corruped database", msgContent);
            LOGGER.error(error);
            throw new PersistenceException(error, ex);
        }
    }
    
    
}
