package eu.domibus.connector.controller.service.impl;

import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.domain.enums.DomibusConnectorMessageDirection;
import eu.domibus.connector.domain.enums.LinkType;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.ecodex.dc5.flow.flows.ReceiveMessageFlow;
import eu.ecodex.dc5.message.model.DC5Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service
//@RequiredArgsConstructor
public class SubmitToConnectorImpl {

//    private final ReceiveMessageFlow receiveMessageFlow;

//    @Override
//    @Transactional
    public void submitToConnector(DC5Message message, DomibusConnectorLinkPartner.LinkPartnerName linkPartner, LinkType linkType) {
        ReceiveMessageFlow.ReceiveMessageFlowResult receiveMessageFlowResult;
        if (linkType == LinkType.GATEWAY) {
            message.setDirection(DomibusConnectorMessageDirection.GATEWAY_TO_BACKEND);
            message.setGatewayLinkName(linkPartner);
//            receiveMessageFlowResult = receiveMessageFlow.receiveMessage(message, (msg, msgProcess) -> msg);
        } else if (linkType == LinkType.BACKEND) {
            message.setDirection(DomibusConnectorMessageDirection.BACKEND_TO_GATEWAY);
            message.setBackendLinkName(linkPartner);
//            receiveMessageFlowResult = receiveMessageFlow.receiveMessage(message, (msg, msgProcess) -> msg);
        } else {
            throw new RuntimeException("linkType not known!");
        }
//        if (!receiveMessageFlowResult.isSuccess()) {
//            throw new RuntimeException("Submission failed due: " + receiveMessageFlowResult.toString(), receiveMessageFlowResult.getError().orElse(null));
//        }
    }

}
