package eu.domibus.connector.gateway.link.jms.impl;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("gwlink-jms")
public class GatewayLinkAsyncSubmitToGwService implements DomibusConnectorGatewaySubmissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorGatewaySubmissionService.class);

//    @Autowired
//    private DomibusConnectorAsyncSubmitToGatewayService submitToGatewayService;

    @Autowired
    private TransportStatusService transportStatusService;

    @Autowired
    DomibusConnectorDomainMessageTransformerService transformerService;

    @Override
    public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException {

        DomibusConnectorMessageType messageType = transformerService.transformDomainToTransition(message);

        //TODO: try catch ...?
//        submitToGatewayService.submitMessage(messageType);
        LOGGER.info("Message [{}] put on queue to gateway - new message state is pending", message.getConnectorMessageId());

        TransportStatusService.DomibusConnectorTransportState transportState =
                new TransportStatusService.DomibusConnectorTransportState();

        transportState.setStatus(TransportState.PENDING);

        transportStatusService.updateTransportToGatewayStatus(new TransportStatusService.TransportId(message.getConnectorMessageId()), transportState);
    }


}
