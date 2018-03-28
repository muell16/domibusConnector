package eu.domibus.connector.persistence.service.impl.helper;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageAttachmentBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageContentBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageDocumentBuilder;
import eu.domibus.connector.domain.model.helper.CopyHelper;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMsgContDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont;
import eu.domibus.connector.persistence.service.PersistenceException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 *
 * Service for persisting message content like:
 * <ul>
 *  <li>message content - the business pdf and xml content</li>
 *  <li>message attachments</li>
 *  <li>message confirmations</li>
 * </ul>
 * 
 * For persisting the java objects are serialized and stored into database, 
 * bigDataReference fields are replaced by plain BigDataReference objects
 * to make sure that the BigDataReferenceObject can be persisted
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
@Transactional
public class MsgContentPersistenceService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(MsgContentPersistenceService.class);
    
    @Autowired
    private DomibusConnectorMsgContDao msgContDao;
    
    @Autowired
    private DomibusConnectorMessageDao msgDao;

    
    /*
     * DAO Setter
     */
    
    public void setMsgContDao(DomibusConnectorMsgContDao msgContDao) {
        this.msgContDao = msgContDao;
    }

    public void setMsgDao(DomibusConnectorMessageDao msgDao) {
        this.msgDao = msgDao;
    }
       
    /*
     * END DAO Setter
     */
    
    
    /**
     * loads Message Content from Database, deserializes the stored objects back to java objects, and puts them back into the message
     * @param messageBuilder
     * @param dbMessage
     * @throws PersistenceException 
     */
    public void loadMsgContent(final @Nonnull DomibusConnectorMessageBuilder messageBuilder, final PDomibusConnectorMessage dbMessage) throws PersistenceException {
        List<PDomibusConnectorMsgCont> findByMessage = this.msgContDao.findByMessage(dbMessage);
        //mapFromDbToDomain content back
        Optional<PDomibusConnectorMsgCont> findFirst = findByMessage.stream()
                .filter( c -> StoreType.MESSAGE_CONTENT.getDbString().equals(c.getContentType()))
                .findFirst();
        if (findFirst.isPresent()) {
            DomibusConnectorMessageContent messageContent = mapFromMsgCont(findFirst.get(), DomibusConnectorMessageContent.class);
            messageBuilder.setMessageContent(messageContent);
        }
        //mapFromDbToDomain evidence back
        findByMessage.stream()
                .filter( c -> StoreType.MESSAGE_CONFIRMATION.getDbString().equals(c.getContentType()))
                .forEach( c -> {
                    DomibusConnectorMessageConfirmation msgConfirmation = mapFromMsgCont(c, DomibusConnectorMessageConfirmation.class);
                    messageBuilder.addConfirmation(msgConfirmation);
                });
        //mapFromDbToDomain attachments back
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
        PDomibusConnectorMessage dbMessage = this.msgDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        if (messageContent != null) {
            toStoreList.add(mapToDb(dbMessage, messageContent));
        }
        //handle attachments
        for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
            toStoreList.add(mapContent(dbMessage, attachment));
        }
        //handle confirmations
        for (DomibusConnectorMessageConfirmation c : message.getMessageConfirmations()) {
            toStoreList.add(mapContent(dbMessage, c));            
        }        
        this.msgContDao.deleteByMessage(dbMessage);   //delete old contents
        this.msgContDao.save(toStoreList); //save new contents
    }        
    
    PDomibusConnectorMsgCont mapToDb(PDomibusConnectorMessage message, DomibusConnectorMessageContent content) {
        //change BigDocumentRefernce with the plain implementation to make sure it is serializeable!
        DomibusConnectorMessageContent copiedMessageContent = CopyHelper.copyMessageContent(content);
        if (content.getDocument() != null) {            
            DomibusConnectorBigDataReference plainRef = createBigDataReferenceCopy(content.getDocument().getDocument());            
            copiedMessageContent.getDocument().setDocument(plainRef);
        }  
        return serializeObjectIntoMsgCont(message, StoreType.MESSAGE_CONTENT, copiedMessageContent);                
    }
    
    PDomibusConnectorMsgCont mapContent(PDomibusConnectorMessage message, DomibusConnectorMessageAttachment attachment) {        
        DomibusConnectorMessageAttachment copiedAttachment = CopyHelper.copyAttachment(attachment);
        DomibusConnectorBigDataReference bigDataReferenceCopy = createBigDataReferenceCopy(copiedAttachment.getAttachment());
        copiedAttachment.setAttachment(bigDataReferenceCopy);
        return serializeObjectIntoMsgCont(message, StoreType.MESSAGE_ATTACHMENT, copiedAttachment);
    }
    
    PDomibusConnectorMsgCont mapContent(PDomibusConnectorMessage message, DomibusConnectorMessageConfirmation confirmation) {       
        return serializeObjectIntoMsgCont(message, StoreType.MESSAGE_CONFIRMATION, confirmation);
    }
    
    DomibusConnectorBigDataReference createBigDataReferenceCopy(DomibusConnectorBigDataReference toCopy) {
        if (toCopy == null) {
            return null;
        }
        DomibusConnectorBigDataReference plainRef = new DomibusConnectorBigDataReference();
        BeanUtils.copyProperties(toCopy, plainRef);
        return plainRef;
    }
    
    
    /**
     * Takes a StoreType and a Object
     * serializes the object and
     * writes that into @see eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont
     * 
     * 
     */
    PDomibusConnectorMsgCont serializeObjectIntoMsgCont(    
            PDomibusConnectorMessage message,
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
            msgCont.setMessage(message);
            return msgCont;
        } catch (IOException ioe) {
            String error = String.format("storeMsgContent: A error occured during serializing [%s] object [%s] and storing it into database", type, object);
            LOGGER.error(error, ioe);
            throw new PersistenceException(error);
        }        
    }
    
    <T> T mapFromMsgCont(@Nonnull PDomibusConnectorMsgCont msgContent, Class<T> clazz) {
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
