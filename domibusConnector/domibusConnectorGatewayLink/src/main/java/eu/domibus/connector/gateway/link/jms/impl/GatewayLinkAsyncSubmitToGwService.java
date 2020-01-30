package eu.domibus.connector.gateway.link.jms.impl;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformer;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.jms.gateway.DomibusConnectorAsyncSubmitToGatewayService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("gwlink-jms")
public class GatewayLinkAsyncSubmitToGwService implements DomibusConnectorGatewaySubmissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorGatewaySubmissionService.class);

    @Autowired
    private DomibusConnectorAsyncSubmitToGatewayService submitToGatewayService;

    @Autowired
    private TransportStatusService transportStatusService;

    @Override
    public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException {

        DomibusConnectorMessageType messageType = DomibusConnectorDomainMessageTransformer.transformDomainToTransition(message);

        //TODO: try catch ...?
        submitToGatewayService.submitMessage(messageType);
        LOGGER.info("Message [{}] put on queue to gateway - new message state is pending", message.getConnectorMessageId());

        TransportStatusService.DomibusConnectorTransportState transportState =
                new TransportStatusService.DomibusConnectorTransportState();

        transportState.setStatus(TransportStatusService.TransportState.PENDING);
        transportState.setConnectorTransportId(message.getConnectorMessageId());

        transportStatusService.updateTransportToGatewayStatus(transportState);
    }


}
