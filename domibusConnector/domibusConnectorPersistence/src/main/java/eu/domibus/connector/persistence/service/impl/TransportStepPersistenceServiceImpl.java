package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.persistence.dao.DomibusConnectorEvidenceDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkPartnerDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorTransportStepDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessage;
import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStep;
import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStepStatusUpdate;
import eu.domibus.connector.persistence.service.TransportStepPersistenceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransportStepPersistenceServiceImpl implements TransportStepPersistenceService {

    @Autowired
    DomibusConnectorTransportStepDao transportStepDao;

    @Autowired
    DomibusConnectorLinkPartnerDao linkPartnerDao;

    @Autowired
    DomibusConnectorMessageDao messageDao;

    @Autowired
    DomibusConnectorEvidenceDao evidenceDao;

    @Override
    public DomibusConnectorTransportStep createNewTransportStep(DomibusConnectorTransportStep transportStep) {
        if (transportStep.getLinkPartnerName() == null || StringUtils.isEmpty(transportStep.getLinkPartnerName().toString())) {
            throw new IllegalArgumentException("LinkPartner name must be set!");
        }
        if (transportStep.getMessageId() == null || StringUtils.isEmpty(transportStep.getMessageId().toString())) {
            throw new IllegalArgumentException("ConnectorMessageId must be set!");
        }

        String msgId = transportStep.getMessageId().toString();
        String linkPartnerName = transportStep.getLinkPartnerName().toString();

        Optional<Integer> nAttempt = transportStepDao.getHighestAttemptBy(msgId, linkPartnerName);
        int nextAttempt = nAttempt.orElse(0) + 1;
        transportStep.setAttempt(nextAttempt);

        transportStep.setTransportId(new TransportStatusService.TransportId(msgId + "_" + linkPartnerName + "_" + nextAttempt));

        PDomibusConnectorTransportStep dbStep = mapTransportStepToDb(transportStep);
        PDomibusConnectorTransportStep savedDbStep = transportStepDao.save(dbStep);

        return transportStep;
    }

    @Override
    public DomibusConnectorTransportStep getTransportStepByTransportId(TransportStatusService.TransportId transportId) {
        Optional<PDomibusConnectorTransportStep> foundTransport = transportStepDao.findByTransportId(transportId);
        if (foundTransport.isPresent()) {
            return mapTransportStepToDomain(foundTransport.get());
        } else {
            throw new RuntimeException(String.format("No Transport found with transport id [%s]", transportId));
        }
    }

    @Override
    public DomibusConnectorTransportStep update(DomibusConnectorTransportStep transportStep) {
        PDomibusConnectorTransportStep dbTransportStep = mapTransportStepToDb(transportStep);
        dbTransportStep = transportStepDao.save(dbTransportStep);
        transportStep = mapTransportStepToDomain(dbTransportStep);
        return transportStep;
    }

    private DomibusConnectorTransportStep mapTransportStepToDomain(PDomibusConnectorTransportStep dbTransportStep) {
        DomibusConnectorTransportStep step = new DomibusConnectorTransportStep();
//        BeanUtils.copyProperties(dbTransportStep, step);
        step.setMessageId(new DomibusConnectorMessage.DomibusConnectorMessageId(dbTransportStep.getConnectorMessageId()));
        step.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName(dbTransportStep.getLinkPartnerName()));
        step.setTransportId(dbTransportStep.getTransportId());
        step.setAttempt(dbTransportStep.getAttempt());
        step.setCreated(dbTransportStep.getCreated());
        step.setRemoteMessageId(dbTransportStep.getRemoteMessageId());
        step.setTransportSystemMessageId(dbTransportStep.getTransportSystemMessageId());

        List<DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate> statusUpdates = dbTransportStep
                .getStatusUpdates()
                .stream()
                .map(this::mapTransportStepState)
                .collect(Collectors.toList());

        step.setStatusUpdates(statusUpdates);

        return step;
    }

    private DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate mapTransportStepState(PDomibusConnectorTransportStepStatusUpdate dbTransportUpdate) {
        DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate update =
                new DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate();
//        BeanUtils.copyProperties(dbTransportUpdate, update);
        update.setCreated(dbTransportUpdate.getCreated());
        update.setText(dbTransportUpdate.getText());
        update.setTransportState(dbTransportUpdate.getTransportState());
        return update;
    }

    private PDomibusConnectorTransportStep mapTransportStepToDb(DomibusConnectorTransportStep transportStep) {

        String msgId = transportStep.getMessageId().getConnectorMessageId();
        String partnerName = transportStep.getLinkPartnerName().getLinkName();

        Optional<PDomibusConnectorTransportStep> foundStep = transportStepDao.findbyMsgLinkPartnerAndAttempt(msgId, partnerName, transportStep.getAttempt());
        PDomibusConnectorTransportStep dbStep = foundStep.orElseGet(  () -> {
            PDomibusConnectorTransportStep s = new PDomibusConnectorTransportStep();
            s.setConnectorMessageId(msgId);
            s.setLinkPartnerName(partnerName);
            s.setTransportId(transportStep.getTransportId());
            return s;
        });

        dbStep.setAttempt(transportStep.getAttempt());
        dbStep.setRemoteMessageId(transportStep.getRemoteMessageId());
        dbStep.setTransportSystemMessageId(transportStep.getTransportSystemMessageId());

        Set<TransportState> updates = dbStep.getStatusUpdates()
                .stream()
                .map(u -> u.getTransportState())
                .collect(Collectors.toSet());

        List<PDomibusConnectorTransportStepStatusUpdate> newStatus = transportStep.getStatusUpdates()
                .stream()
                //if db not alredy contains update add it
                .filter(u -> !updates.contains(u.getTransportState()))
                .map(u -> {
                    PDomibusConnectorTransportStepStatusUpdate update = new PDomibusConnectorTransportStepStatusUpdate();
                    update.setCreated(u.getCreated());
                    update.setTransportState(u.getTransportState());
                    update.setText(u.getText());
                    update.setTransportStep(dbStep);
                    return update;
                }).collect(Collectors.toList());

        dbStep.getStatusUpdates().addAll(newStatus);

        return dbStep;
    }

}
