package eu.domibus.connector.gateway.link.jms;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class GatewaySubmissionTransportStatusServiceImpl implements  GatewaySubmissionTransportStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewaySubmissionTransportStatusServiceImpl.class);

    @Override
    public void setTransportStatusForTransportToGateway(DomibusConnectorTransportState transportState) {
        LOGGER.debug("Set transportState.... [{}]", transportState);

        //TODO: mark message as transported in db, inform controller, ...


    }
}
