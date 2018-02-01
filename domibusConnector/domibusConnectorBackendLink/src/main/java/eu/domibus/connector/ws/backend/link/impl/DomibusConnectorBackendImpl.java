
package eu.domibus.connector.ws.backend.link.impl;

import eu.domibus.connector.domain.transition.DomibsConnectorAcknowledgementType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageType;
import eu.domibus.connector.domain.transition.DomibusConnectorMessagesType;
import eu.domibus.connector.ws.backend.webservice.DomibusConnectorBackendWebService;
import eu.domibus.connector.ws.backend.webservice.EmptyRequestType;
import java.security.Principal;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Service("connectorBackendImpl")
public class DomibusConnectorBackendImpl implements DomibusConnectorBackendWebService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorBackendWebService.class);
    
    @Resource
    WebServiceContext wsContext;
    
    @Override
    public DomibusConnectorMessagesType requestMessages(EmptyRequestType requestMessagesRequest) {
        //TODO: return all pending messages for the calling client
        return null;
    }

    @Override
    public DomibsConnectorAcknowledgementType submitMessage(DomibusConnectorMessageType submitMessageRequest) {
        //TODO: submit message
        LOGGER.debug("#submitMessage: message: [{}]", submitMessageRequest);
        
        
        
        //TODO: get user, password, certificate
        //System.out.println("HALLO WELT I RCV A MESSAGE!");
        Principal userPrincipal = wsContext.getUserPrincipal();
        if (userPrincipal != null) {
            LOGGER.info("Message from user: [{}]", userPrincipal.getName());
            
            //TODO: transform message & associate backendId (userPrincipal) with message
            
            //TODO: validate if this user can use this message service?
                        
            //TODO: store message
            
            //TODO: store message content
            
            //TODO: put transformed message on internal queue for further processing
            
            
        } else {
            throw new RuntimeException("message from unkown user!");
        }
        
        
        
        
        DomibsConnectorAcknowledgementType answer = new DomibsConnectorAcknowledgementType();
        answer.setResult(true);
        return answer;
    }

}
