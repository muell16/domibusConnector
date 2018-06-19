package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageErrorDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageError;
import eu.domibus.connector.persistence.service.DomibusConnectorMessageErrorPersistenceService;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DomibusConnectorMessageErrorPersistenceServiceImpl implements DomibusConnectorMessageErrorPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorMessageErrorPersistenceServiceImpl.class);

    private DomibusConnectorMessageErrorDao messageErrorDao;
    private DomibusConnectorMessageDao messageDao;

    @Autowired
    public void setMessageErrorDao(DomibusConnectorMessageErrorDao messageErrorDao) {
        this.messageErrorDao = messageErrorDao;
    }

    @Autowired
    public void setMessageDao(DomibusConnectorMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    @Transactional
    public void persistMessageError(String connectorMessageId, DomibusConnectorMessageError messageError) {
        PDomibusConnectorMessageError dbError = new PDomibusConnectorMessageError();

        PDomibusConnectorMessage msg = messageDao.findOneByConnectorMessageId(connectorMessageId);
        if (msg != null) {
            dbError.setMessage(msg);
            dbError.setErrorMessage(messageError.getText());
            dbError.setDetailedText(messageError.getDetails());
            dbError.setErrorSource(messageError.getSource());

            this.messageErrorDao.save(dbError);
        }
    }

    @Override
    @Transactional
    @SuppressWarnings("squid:MethodCyclomaticComplexity")
    public void persistMessageErrorFromException(DomibusConnectorMessage message, Throwable ex, Class<?> source) {
        if (message == null) {
            throw new IllegalArgumentException("Cannot persist a exception for a null, message is not allowed to be null!");
        }
        if (ex == null) {
            throw new IllegalArgumentException("Message Error cannot be stored as there is no exception given!");
        }
        if (source == null) {
            throw new IllegalArgumentException(
                    "Message Error cannot be stored as the Class object given as source is null!");
        }

        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        if (dbMessage == null) {
            throw new PersistenceException(
                    String.format("No entry for message [%s] has been found in database! Persist message first!", message));
        }

        PDomibusConnectorMessageError error = new PDomibusConnectorMessageError();
        error.setErrorMessage(ex.getMessage());
        error.setDetailedText(ExceptionUtils.getStackTrace(ex));
        error.setErrorSource(source.getName());
        error.setMessage(dbMessage);

        this.messageErrorDao.save(error);
    }

    @Override
    public List<DomibusConnectorMessageError> getMessageErrors(DomibusConnectorMessage message) throws PersistenceException {
        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        if (dbMessage == null) {
            //no message reference
            return new ArrayList<>();
        }
        List<PDomibusConnectorMessageError> dbErrorsForMessage = this.messageErrorDao.findByMessage(dbMessage.getId());
        if (!CollectionUtils.isEmpty(dbErrorsForMessage)) {
            List<DomibusConnectorMessageError> messageErrors = new ArrayList<>(dbErrorsForMessage.size());

            for (PDomibusConnectorMessageError dbMsgError : dbErrorsForMessage) {
                DomibusConnectorMessageError msgError
                        = new DomibusConnectorMessageError(
                        dbMsgError.getErrorMessage(),
                        dbMsgError.getDetailedText(),
                        dbMsgError.getErrorSource()
                );
                messageErrors.add(msgError);
            }

            return messageErrors;
        }
        return new ArrayList<>();
    }

}
