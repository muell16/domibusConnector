package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.model.DomibusConnectorTransportStep;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkPartnerDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageDao;
import eu.domibus.connector.persistence.dao.DomibusConnectorTransportStepDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStep;
import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStepStatusUpdate;
import eu.domibus.connector.persistence.service.TransportStepPersistenceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

        PDomibusConnectorTransportStep dbStep = mapTransportStepToDb(transportStep);

        PDomibusConnectorTransportStep savedDbStep = transportStepDao.save(dbStep);

        transportStep.setTransportId(new TransportStatusService.TransportId(msgId + "_" + linkPartnerName + "_" + nextAttempt));

        return transportStep;
    }

    @Override
    public DomibusConnectorTransportStep getTransportStepByTransportId(TransportStatusService.TransportId transportId) {
        Optional<DomibusConnectorTransportStep> foundTransport = transportStepDao.findByTransportId(transportId.toString());
        return foundTransport.get();
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
        BeanUtils.copyProperties(dbTransportStep, step);

        List<DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate> statusUpdates = dbTransportStep
                .getStatusUpdates()
                .stream()
                .map(this::mapTransportStepState)
                .collect(Collectors.toList());

        step.setStatusUpdates(statusUpdates);

        return step;
    }

    private DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate mapTransportStepState(PDomibusConnectorTransportStepStatusUpdate dbTransportUpdate) {
        DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate update = new DomibusConnectorTransportStep.DomibusConnectorTransportStepStatusUpdate();
        BeanUtils.copyProperties(dbTransportUpdate, update);
        return update;
    }

    private PDomibusConnectorTransportStep mapTransportStepToDb(DomibusConnectorTransportStep transportStep) {

        String msgId = transportStep.getMessageId().getConnectorMessageId();
        String partnerName = transportStep.getLinkPartnerName().getLinkName();

        Optional<PDomibusConnectorTransportStep> foundStep = transportStepDao.findbyMsgLinkPartnerAndAttempt(msgId, partnerName, transportStep.getAttempt());
        PDomibusConnectorTransportStep step = foundStep.orElseGet(  () -> {
            PDomibusConnectorTransportStep s = new PDomibusConnectorTransportStep();
            s.setMessage(messageDao.findOneByConnectorMessageId(msgId));
            s.setLinkPartnerName(partnerName);
            return s;
        });

        BeanUtils.copyProperties(transportStep, step);

        return step;
    }

}
