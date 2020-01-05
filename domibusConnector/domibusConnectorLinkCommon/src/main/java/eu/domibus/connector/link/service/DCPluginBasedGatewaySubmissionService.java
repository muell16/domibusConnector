package eu.domibus.connector.link.service;

import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.service.DomibusConnectorGatewaySubmissionService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("linkplugins")
public class DCPluginBasedGatewaySubmissionService implements DomibusConnectorGatewaySubmissionService {

    @Autowired
    DCActiveLinkManagerService linkManager;


    @Override
    public void submitToGateway(DomibusConnectorMessage message) throws DomibusConnectorGatewaySubmissionException {
//        linkManager.getSubmitToLinkPartner()
        //TODO: implement!
        throw new RuntimeException("not implemented yet!");
    }
}
