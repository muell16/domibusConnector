package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.persistence.dao.DomibusConnectorActionDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageInfoDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorPartyDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.persistence.model.*;
import eu.domibus.connector.persistence.service.DomibusConnectorPModeService;
import eu.domibus.connector.persistence.service.DomibusConnectorPartyPersistenceService;
import eu.domibus.connector.persistence.service.exceptions.PersistenceException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Component
public class InternalMessageInfoPersistenceServiceImpl implements InternalMessageInfoPersistenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalMessageInfoPersistenceServiceImpl.class);

    private DomibusConnectorMessageInfoDao messageInfoDao;
    private DomibusConnectorPModePersistenceService pModeService;
//    private DomibusConnectorPartyDao partyDao;
//    private DomibusConnectorPartyPersistenceServiceImpl partyPersistenceService;
//    private DomibusConnectorPModeService pModeService;
//    private DomibusConnectorServiceDao serviceDao;
//    private DomibusConnectorActionDao actionDao;

    @Autowired
    public void setMessageInfoDao(DomibusConnectorMessageInfoDao messageInfoDao) {
        this.messageInfoDao = messageInfoDao;
    }

    @Autowired
    public void setDomibusConnectorPModePersistenceService(DomibusConnectorPModePersistenceService pModeService) {
        this.pModeService = pModeService;
    }

//    @Autowired
//    public void setpModeService(DomibusConnectorPModeService pModeService) {
//        this.pModeService = pModeService;
//    }

    //    @Autowired
//    public void setDomibusConnectorPartyPersistenceServiceImpl(DomibusConnectorPartyPersistenceServiceImpl partyPersistenceService) {
//        this.partyPersistenceService = partyPersistenceService;
//    }

//    @Autowired
//    public void setServiceDao(DomibusConnectorServiceDao serviceDao) {
//        this.serviceDao = serviceDao;
//    }
//
//    @Autowired
//    public void setActionDao(DomibusConnectorActionDao actionDao) {
//        this.actionDao = actionDao;
//    }

    @Override
    @Transactional
    public void persistMessageInfo(DomibusConnectorMessage message, PDomibusConnectorMessage dbMessage) throws PersistenceException {
        try {
            PDomibusConnectorMessageInfo dbMessageInfo = new PDomibusConnectorMessageInfo();
            dbMessageInfo.setMessage(dbMessage);
            dbMessageInfo.setCreated(new Date());
            dbMessageInfo.setUpdated(new Date());
            mapMessageDetailsToDbMessageInfoPersistence(message.getMessageDetails(), dbMessageInfo);


            dbMessageInfo = this.messageInfoDao.save(dbMessageInfo);
            dbMessageInfo = messageInfoDao.findById(dbMessageInfo.getId()).get();
            this.validatePartyServiceActionOfMessageInfo(dbMessageInfo);
            dbMessage.setMessageInfo(dbMessageInfo);

            mapMessageInfoIntoMessageDetails(dbMessage, message.getMessageDetails());

        } catch (Exception e) {
            LOGGER.error("Exception occured", e);
            throw new PersistenceException("Could not persist message info into database. ", e);
        }
    }

    /*
     * TODO: this validation code is at the wrong place
     *   it should already check at the very beginning - before the message is being persisted!
     *
     */
    @Override
    public PDomibusConnectorMessageInfo validatePartyServiceActionOfMessageInfo(PDomibusConnectorMessageInfo messageInfo) throws PersistenceException {
        DomibusConnectorMessageLane.MessageLaneId defaultMessageLaneId = DomibusConnectorMessageLane.getDefaultMessageLaneId();
        PDomibusConnectorAction dbAction = messageInfo.getAction();
        Optional<PDomibusConnectorAction> dbActionFound = pModeService.getConfiguredSingleDB(defaultMessageLaneId, messageInfo.getAction());
        checkNull(dbAction, dbActionFound,
                String.format("No action [%s] is configured at the connector!", dbAction.getId()));
        messageInfo.setAction(dbActionFound.get());

        PDomibusConnectorService dbService = messageInfo.getService();
        Optional<PDomibusConnectorService> dbServiceFound = pModeService.getConfiguredSingleDB(defaultMessageLaneId, messageInfo.getService());
        checkNull(dbService, dbServiceFound,
                String.format("No service [%s] is configured at the connector!", dbService.getId()));
        messageInfo.setService(dbServiceFound.get());

        PDomibusConnectorParty dbFromParty = messageInfo.getFrom();
        Optional<PDomibusConnectorParty> dbFromPartyFound = pModeService.getConfiguredSingleDB(defaultMessageLaneId, dbFromParty);
        checkNull(dbFromParty, dbFromPartyFound,
                String.format("No party [%s] is configured at the connector!", dbFromParty));
        messageInfo.setFrom(dbFromPartyFound.get());

        PDomibusConnectorParty dbToParty = messageInfo.getTo();
        Optional<PDomibusConnectorParty> dbToPartyFound = pModeService.getConfiguredSingleDB(defaultMessageLaneId, dbToParty);
        checkNull(dbToParty, dbToPartyFound,
                String.format("No party [%s] is configured at the connector!", dbToParty));
        messageInfo.setTo(dbToPartyFound.get());

        return messageInfo;
    }

    private void checkNull(Object provided, Optional foundInDb, String errorMessage) throws PersistenceException {
        if (! foundInDb.isPresent()) {
            String error = String.format("%s [%s] is not configured in database!", provided.getClass().getSimpleName(), provided);
            //TÒDO: BUSINESS_LOG
            LOGGER.error("{} Check your p-modes or reimport them into the connector.", errorMessage);
            throw new PersistenceException(error);
        }
    }


    /**
     * maps all messageInfos into the message details
     *  *) action
     *  *) service
     *  *) originalSender
     *  *) finalRecipient
     *  *) fromParty
     *  *) toParty
     *
     * @param dbMessage the db message
     * @param details the details, wich are changed
     * @return - the reference of the changed details (same reference as passed via param details)
     */
    @Override
    public DomibusConnectorMessageDetails mapMessageInfoIntoMessageDetails(PDomibusConnectorMessage dbMessage, DomibusConnectorMessageDetails details) {
        PDomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
        if (messageInfo != null) {

            PDomibusConnectorAction dbAction = messageInfo.getAction();
            DomibusConnectorAction action = ActionMapper.mapActionToDomain(dbAction);
            details.setAction(action);

            PDomibusConnectorService dbService = messageInfo.getService();
            DomibusConnectorService service = ServiceMapper.mapServiceToDomain(dbService);
            details.setService(service);

            details.setFinalRecipient(messageInfo.getFinalRecipient());
            details.setOriginalSender(messageInfo.getOriginalSender());

            PDomibusConnectorParty fromPartyDb = messageInfo.getFrom();
            DomibusConnectorParty fromParty = PartyMapper.mapPartyToDomain(fromPartyDb);
            LOGGER.trace("#mapMessageInfoIntoMessageDetails: set fromParty to [{}]", fromParty);
            details.setFromParty(fromParty);

            PDomibusConnectorParty dbToParty = messageInfo.getTo();
            DomibusConnectorParty toParty = PartyMapper.mapPartyToDomain(dbToParty);
            LOGGER.trace("#mapMessageInfoIntoMessageDetails: set toParty to [{}]", toParty);
            details.setToParty(toParty);
        }
        return details;
    }

    @Override
    public void mapMessageDetailsToDbMessageInfoPersistence(DomibusConnectorMessageDetails messageDetails, PDomibusConnectorMessageInfo dbMessageInfo) {
        PDomibusConnectorAction persistenceAction = ActionMapper.mapActionToPersistence(messageDetails.getAction());
        dbMessageInfo.setAction(persistenceAction);

        PDomibusConnectorService persistenceService = ServiceMapper.mapServiceToPersistence(messageDetails.getService());
        dbMessageInfo.setService(persistenceService);

        dbMessageInfo.setFinalRecipient(messageDetails.getFinalRecipient());
        dbMessageInfo.setOriginalSender(messageDetails.getOriginalSender());

        PDomibusConnectorParty from = PartyMapper.mapPartyToPersistence(messageDetails.getFromParty());
        dbMessageInfo.setFrom(from);
        PDomibusConnectorParty to = PartyMapper.mapPartyToPersistence(messageDetails.getToParty());
        dbMessageInfo.setTo(to);
    }

    @Override
    public void mergeMessageInfo(DomibusConnectorMessage message, PDomibusConnectorMessage dbMessage) {
        PDomibusConnectorMessageInfo messageInfo = dbMessage.getMessageInfo();
        if (messageInfo == null) {
            messageInfo = new PDomibusConnectorMessageInfo();
            dbMessage.setMessageInfo(messageInfo);
        }

        DomibusConnectorMessageDetails messageDetails = message.getMessageDetails();

        if (messageDetails != null) {
//            this.internalMessageInfoPersistenceService.mergeMessageInfo(message, dbMessage);
            mapMessageDetailsToDbMessageInfoPersistence(message.getMessageDetails(), messageInfo);
            messageInfoDao.save(messageInfo);
        }
    }

}
