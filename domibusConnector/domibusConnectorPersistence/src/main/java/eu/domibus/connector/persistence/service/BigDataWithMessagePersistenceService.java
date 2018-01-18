/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.service;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

/**
 * Facade Service to make it easier to persist all big data of 
 * a message
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Service
public class BigDataWithMessagePersistenceService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(BigDataWithMessagePersistenceService.class);
    
    @Autowired
    private DomibusConnectorBigDataPersistenceService bigDataPersistenceServiceImpl;
    
    // START GETTER / SETTER //
    public DomibusConnectorBigDataPersistenceService getBigDataPersistenceServiceImpl() {
        return bigDataPersistenceServiceImpl;
    }

    public void setBigDataPersistenceServiceImpl(DomibusConnectorBigDataPersistenceService bigDataPersistenceServiceImpl) {
        this.bigDataPersistenceServiceImpl = bigDataPersistenceServiceImpl;
    }
    // ENDE GETTER / SETTER //
    
    
    public DomibusConnectorMessage persistAllBigFilesFromMessage(DomibusConnectorMessage message) {
        try {
            for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {                   
                DomibusConnectorBigDataReference readFrom = attachment.getAttachment();
                DomibusConnectorBigDataReference writeTo = bigDataPersistenceServiceImpl.createDomibusConnectorBigDataReference(message);
                StreamUtils.copy(readFrom.getInputStream(), writeTo.getOutputStream());                
            }
            DomibusConnectorMessageContent messageContent = message.getMessageContent();
            if (containsMainDocument(messageContent)) {
                DomibusConnectorBigDataReference docReadFrom = messageContent.getDocument().getDocument();
                DomibusConnectorBigDataReference docWriteTo = bigDataPersistenceServiceImpl.createDomibusConnectorBigDataReference(message);
                StreamUtils.copy(docReadFrom.getInputStream(), docWriteTo.getOutputStream());
            }
            return message;
        } catch (IOException ioe) {
            throw new PersistenceException("A exception occured during copying big data into storage", ioe);
        }
    }
    
    public DomibusConnectorMessage loadAllBigFilesFromMessage(DomibusConnectorMessage message) {
        for (DomibusConnectorMessageAttachment attachment : message.getMessageAttachments()) {
            DomibusConnectorBigDataReference activeRead = attachment.getAttachment();
            DomibusConnectorBigDataReference activatedRead = bigDataPersistenceServiceImpl.getReadableDataSource(activeRead);
            //attachment.setAttachment(activatedRead);
        }
        DomibusConnectorMessageContent messageContent = message.getMessageContent();
        if (containsMainDocument(messageContent)) {
            DomibusConnectorBigDataReference docRefresRead = messageContent.getDocument().getDocument();
            DomibusConnectorBigDataReference docActivatedRead = bigDataPersistenceServiceImpl.getReadableDataSource(docRefresRead);
            //messageContent.getDocument().setDocument(docActivatedRead);
        }
        
        return message;
    }
        
    private boolean containsMainDocument(DomibusConnectorMessageContent content) {
        return content != null && content.getDocument() != null && content.getDocument().getDocument() != null;
    }
 

}
