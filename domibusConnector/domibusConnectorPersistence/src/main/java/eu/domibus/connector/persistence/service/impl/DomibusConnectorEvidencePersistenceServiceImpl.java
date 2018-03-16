package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.PersistenceException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class DomibusConnectorEvidencePersistenceServiceImpl implements DomibusConnectorEvidencePersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorEvidencePersistenceServiceImpl.class);

    private DomibusConnectorEvidenceDao evidenceDao;
    private DomibusConnectorMessageDao messageDao;

    @Autowired
    public void setEvidenceDao(DomibusConnectorEvidenceDao evidenceDao) {
        this.evidenceDao = evidenceDao;
    }

    @Autowired
    public void setMessageDao(DomibusConnectorMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void setEvidenceDeliveredToGateway(@Nonnull DomibusConnectorMessage message, @Nonnull eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType evidenceType) throws PersistenceException {
        if (message == null) {
            throw new IllegalArgumentException("message is not allowed to be null!");
        }
//        this.mergeMessageWithDatabase(message);
        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
        Long dbMessageId = dbMessage.getId();
        List<PDomibusConnectorEvidence> evidences = evidenceDao.findByMessage_Id(dbMessageId);
        PDomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
        if (dbEvidence != null) {
            evidenceDao.setDeliveredToGateway(dbEvidence.getId());
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void setEvidenceDeliveredToNationalSystem(@Nonnull DomibusConnectorMessage message, @Nonnull eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType evidenceType) throws PersistenceException {
        if (message == null) {
            throw new IllegalArgumentException("message is not allowed to be null!");
        }
        LOGGER.trace("#setEvidenceDeliveredToNationalSystem: setting evidence [{}] as delivered");
        //this.mergeMessageWithDatabase(message);
        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);

    }

    private boolean setEvidenceDeliveredToNationalSystem(PDomibusConnectorMessage dbMessage, DomibusConnectorEvidenceType evidenceType) {
        List<PDomibusConnectorEvidence> evidences = evidenceDao.findByMessage_Id(dbMessage.getId());
        PDomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
        if (dbEvidence != null) {
            int updated = evidenceDao.setDeliveredToBackend(dbEvidence.getId());
            return updated == 1;
        }
        return false;
    }

    private @Nullable
    PDomibusConnectorEvidence findEvidence(@Nonnull List<PDomibusConnectorEvidence> evidences, @Nonnull DomibusConnectorEvidenceType evidenceType) {
        for (PDomibusConnectorEvidence evidence : evidences) {
            if (evidence.getType().name().equals(evidenceType.name())) {
                return evidence;
            }
        }
        LOGGER.warn("Evidence of type [{}] was not found in evidences [{}]", evidenceType, evidences);
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    @Transactional
    public void persistEvidenceForMessageIntoDatabase(DomibusConnectorMessage message, @Nullable byte[] evidence, DomibusConnectorEvidenceType evidenceType) {

        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);
        if (dbMessage == null) {
            throw new IllegalStateException(String.format("The provided message [%s] does not exist in storage!", message));
        }

        PDomibusConnectorEvidence dbEvidence = new PDomibusConnectorEvidence();

        dbEvidence.setMessage(dbMessage);
        if (evidence != null) {
            dbEvidence.setEvidence(convertByteArrayToString(evidence));
        }
        dbEvidence.setType(eu.domibus.connector.persistence.model.enums.EvidenceType.valueOf(evidenceType.name()));
        dbEvidence.setDeliveredToGateway(null);
        dbEvidence.setDeliveredToNationalSystem(null);
        evidenceDao.save(dbEvidence);
    }

    private PDomibusConnectorMessage findMessageByMessage(DomibusConnectorMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null!");
        }
        if (message.getConnectorMessageId() == null) {
            throw new IllegalArgumentException("the connectorId of the message must be not null!");
        }
        return messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
    }

    private @Nullable String convertByteArrayToString(@Nullable byte[] bytes) {
        try {
            if (bytes == null) {
                return null;
            }
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}