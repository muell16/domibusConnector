package eu.domibus.connector.controller.queues.listener;


import eu.ecodex.dc5.domain.CurrentBusinessDomain;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import static eu.domibus.connector.controller.queues.JmsConfiguration.TO_LINK_QUEUE_BEAN;

@Component
public class ToLinkPartnerListener {

    private static final Logger LOGGER = LogManager.getLogger(ToLinkPartnerListener.class);

//    private final SubmitToLinkService submitToLink;


    @JmsListener(destination = TO_LINK_QUEUE_BEAN)
    @Transactional(rollbackFor = Exception.class)
    @eu.domibus.connector.lib.logging.MDC(name = LoggingMDCPropertyNames.MDC_DC_QUEUE_LISTENER_PROPERTY_NAME, value = "ToLinkPartnerListener")
    public void handleMessage(DC5Message message) {
        String messageId = message.getConnectorMessageId().toString();
        MDC.MDCCloseable mdcCloseable = MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, messageId);
        try {
            CurrentBusinessDomain.setCurrentBusinessDomain(message.getMessageLaneId());
//            submitToLink.submitToLink(message);
        } catch (Exception exc) {
            LOGGER.error("Cannot submit to link, throwing exception, transaction is rollback, Check DLQ.", exc);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw exc;
        } finally {
            CurrentBusinessDomain.setCurrentBusinessDomain(null);
            mdcCloseable.close();
        }
    }
}
