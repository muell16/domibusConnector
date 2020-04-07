package eu.domibus.connector.link.impl.wsplugin;

import eu.domibus.connector.controller.exception.DomibusConnectorControllerException;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.link.api.ActiveLinkPartner;
import eu.domibus.connector.tools.logging.SetMessageOnLoggingContext;
import eu.domibus.connector.ws.gateway.delivery.webservice.DomibusConnectorGatewayDeliveryWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

//@Service(DCGatewayDeliveryEndpointImpl.BEAN_NAME)
public class DCGatewayDeliveryEndpointImpl implements DomibusConnectorGatewayDeliveryWebService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DCGatewayDeliveryEndpointImpl.class);

    public static final String BEAN_NAME = "domibusConnectorDeliveryServiceImpl";


    @Autowired
    DCWsEndpointAuthentication endpointAuthenticator;

    @Autowired
    SubmitToConnector submitToConnector;

    @Autowired
    DomibusConnectorDomainMessageTransformerService transformerService;

    private WebServiceContext webServiceContext;

    @Resource
    public void setWsContext(WebServiceContext webServiceContext) {
        this.webServiceContext = webServiceContext;
    }

    @Override
    public DomibsConnectorAcknowledgementType deliverMessage(DomibusConnectorMessageType deliverMessageRequest) {
        LOGGER.debug("#deliverMessage: deliverRequest [{}] from gw received", deliverMessageRequest);
        ActiveLinkPartner activeLinkPartner = endpointAuthenticator.checkBackendClient(webServiceContext);

        DomibusConnectorMessage domainMessage = transformerService.transformTransitionToDomain(deliverMessageRequest);
        SetMessageOnLoggingContext.putConnectorMessageIdOnMDC(domainMessage.getConnectorMessageId());
        DomibsConnectorAcknowledgementType ack = new DomibsConnectorAcknowledgementType();
        try {
//            controllerService.deliverMessageFromGatewayToController(domainMessage);
            submitToConnector.submitToConnector(domainMessage, activeLinkPartner.getLinkPartner());
        } catch (DomibusConnectorControllerException e) {
            LOGGER.warn("#deliverMessage: failed to process message in controller!", e);
            ack.setResult(false);
            ack.setResultMessage("Message could not be processed! " + e.getMessage());
            return ack;
        }
        ack.setResult(true);
        return ack;
    }

}
