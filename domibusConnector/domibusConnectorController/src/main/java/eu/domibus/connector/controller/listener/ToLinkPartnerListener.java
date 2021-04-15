package eu.domibus.connector.controller.listener;


import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.queues.ToLinkQueue;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageError;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageErrorBuilder;
import eu.domibus.connector.tools.LoggingMDCPropertyNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static eu.domibus.connector.controller.queues.QueuesConfiguration.TO_LINK_QUEUE_BEAN;

@Component
public class ToLinkPartnerListener {

    private static final Logger LOGGER = LogManager.getLogger(ToLinkPartnerListener.class);

    private final SubmitToLinkService submitToLink;
    private final ToLinkQueue toLinkQueue;

    public ToLinkPartnerListener(SubmitToLinkService submitToLink, ToLinkQueue toLinkQueue) {
        this.submitToLink = submitToLink;
        this.toLinkQueue = toLinkQueue;
    }

    @JmsListener(destination = TO_LINK_QUEUE_BEAN)
    @Transactional
    @eu.domibus.connector.lib.logging.MDC(name = LoggingMDCPropertyNames.MDC_DC_QUEUE_LISTENER_PROPERTY_NAME, value = "ToLinkPartnerListener")
    public void handleMessage(DomibusConnectorMessage message) {
        String messageId = message.getConnectorMessageId().toString();
        try (MDC.MDCCloseable mdcCloseable = MDC.putCloseable(LoggingMDCPropertyNames.MDC_DOMIBUS_CONNECTOR_MESSAGE_ID_PROPERTY_NAME, messageId)) {
            submitToLink.submitToLink(message);
        } catch (DomibusConnectorSubmitToLinkException exc) {
            LOGGER.error("Cannot submit to link, putting message on error queue", exc);
            DomibusConnectorMessageError build = DomibusConnectorMessageErrorBuilder.createBuilder()
                    .setText("Cannot submit to link, putting message on error queue")
                    .setDetails(exc)
                    .setSource(ToLinkPartnerListener.class)
                    .build();
            message.getMessageProcessErrors().add(build);
            toLinkQueue.putOnErrorQueue(message);
        }
    }
}
