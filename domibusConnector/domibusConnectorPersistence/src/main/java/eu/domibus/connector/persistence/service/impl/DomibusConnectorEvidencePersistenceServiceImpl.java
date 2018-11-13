package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.domain.model.helper.DomainModelHelper;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorEvidence;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.enums.EvidenceType;
import eu.domibus.connector.persistence.service.DomibusConnectorEvidencePersistenceService;
import eu.domibus.connector.persistence.service.exceptions.EvidencePersistenceException;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import eu.domibus.connector.persistence.service.impl.helper.EvidenceTypeMapper;
import eu.domibus.connector.persistence.service.impl.helper.MapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Service
public class DomibusConnectorEvidencePersistenceServiceImpl implements DomibusConnectorEvidencePersistenceService, InternalEvidencePersistenceService {

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



    @Override
    public void persistEvidenceForMessageIntoDatabase(@Nonnull DomibusConnectorMessage message, byte[] evidence, @Nonnull DomibusConnectorEvidenceType evidenceType, DomibusConnectorMessage.DomibusConnectorMessageId transport) {
        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);
        if (dbMessage == null) {
            throw new IllegalStateException(String.format("The provided message [%s] does not exist in storage!", message));
        }
        this.persistEvidenceForMessageIntoDatabase(dbMessage, evidence, evidenceType, message.getConnectorMessageId());
    }

//    /**
//     * {@inheritDoc }
//     */
////    @Override
//    @Transactional
//    public void setEvidenceDeliveredToGateway(@Nonnull DomibusConnectorMessage message, @Nonnull DomibusConnectorEvidenceType evidenceType) throws PersistenceException {
//        if (message == null) {
//            throw new IllegalArgumentException("message is not allowed to be null!");
//        }
//        PDomibusConnectorMessage dbMessage = messageDao.findOneByConnectorMessageId(message.getConnectorMessageId());
//        Long dbMessageId = dbMessage.getId();
//        List<PDomibusConnectorEvidence> evidences = evidenceDao.findByMessage_Id(dbMessageId);
//        PDomibusConnectorEvidence dbEvidence = findEvidence(evidences, evidenceType);
//        if (dbEvidence != null) {
//            evidenceDao.setDeliveredToGateway(dbEvidence.getId());
//        }
//    }

    @Override
    public void setEvidenceDeliveredToGateway(DomibusConnectorMessage.DomibusConnectorMessageId transport) {
        List<PDomibusConnectorEvidence> evidences = evidenceDao.findByConnectorMessageId(transport.getConnectorMessageId());
    }

    @Override
    public void setEvidenceDeliveredToNationalSystem(DomibusConnectorMessage.DomibusConnectorMessageId transport) {
        evidenceDao.setDeliveredToBackend(transport.getConnectorMessageId());
    }

//    /**
//     * {@inheritDoc }
//     */
//    @Override
//    @Transactional
//    public void setEvidenceDeliveredToNationalSystem(@Nonnull DomibusConnectorMessage message, @Nonnull DomibusConnectorEvidenceType evidenceType) throws PersistenceException {
//        if (message == null) {
//            throw new IllegalArgumentException("message is not allowed to be null!");
//        }
//        LOGGER.trace("#setEvidenceDeliveredToNationalSystem: setting evidence [{}] as delivered");
//        PDomibusConnectorMessage dbMessage = findMessageByMessage(message);
//        if (dbMessage != null) {
//            this.evidenceDao.setDeliveredToBackend(dbMessage, EvidenceTypeMapper.mapEvidenceTypeFromDomainToDb(evidenceType));
//        } else {
//            LOGGER.error("Message [{}] does not exist in database, cannot set evidence as delivered!");
//        }
//    }

    @Override
    @Transactional
    public DomibusConnectorMessage persistAsEvidence(DomibusConnectorMessage message) {
        if (!DomainModelHelper.isEvidenceMessage(message)) {
            throw new IllegalArgumentException("The provided message is NOT an evidence message!");
        }
        DomibusConnectorMessageConfirmation confirmation = message.getMessageConfirmations().get(0);
        String refToMessageId = message.getMessageDetails().getRefToMessageId();
        PDomibusConnectorMessage referencedMessage = messageDao.findOneByEbmsMessageIdOrBackendMessageId(refToMessageId);
        if (referencedMessage == null) {
            String error = String.format("No message with ebmsId or backendId with [%s] found in database! " +
                    "Cannot persist confirmation [%s]", refToMessageId, confirmation);
            throw new EvidencePersistenceException(error);

        }
        this.persistEvidenceForMessageIntoDatabase(referencedMessage, confirmation.getEvidence(), confirmation.getEvidenceType(), message.getConnectorMessageId());
        return message;
    }

    @Nullable
    private PDomibusConnectorEvidence findEvidence(@Nonnull List<PDomibusConnectorEvidence> evidences, @Nonnull DomibusConnectorEvidenceType evidenceType) {
        for (PDomibusConnectorEvidence evidence : evidences) {
            if (evidence.getType().name().equals(evidenceType.name())) {
                return evidence;
            }
        }
        LOGGER.warn("Evidence of type [{}] was not found in evidences [{}]", evidenceType, evidences);
        return null;
    }

//    /**
//     * {@inheritDoc }
//     */
//    @Override
//    @Transactional
//    public void persistEvidenceForMessageIntoDatabase(DomibusConnectorMessage message, @Nullable byte[] evidence, DomibusConnectorEvidenceType evidenceType) {

//    }

    @Transactional
    public void persistEvidenceForMessageIntoDatabase(PDomibusConnectorMessage dbMessage, @Nullable byte[] evidence, DomibusConnectorEvidenceType evidenceType, String transport) {
        PDomibusConnectorEvidence dbEvidence;

        EvidenceType dbEvidenceType = EvidenceTypeMapper.mapEvidenceTypeFromDomainToDb(evidenceType);
        dbEvidence = evidenceDao.findByMessageAndEvidenceType(dbMessage, dbEvidenceType);
        if (dbEvidence == null) {
            LOGGER.trace("Creating new evidence in database!");
            dbEvidence = new PDomibusConnectorEvidence();
        } else {
            LOGGER.trace("updating evidence [{}] in database", dbEvidence);
        }

        dbEvidence.setMessage(dbMessage);
        if (evidence != null) {
            dbEvidence.setEvidence(MapperHelper.convertByteArrayToString(evidence));
        }
        dbEvidence.setType(dbEvidenceType);
        dbEvidence.setConnectorMessageId(transport);

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


}
