package eu.domibus.connector.link.common;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Autowired;

public class CloseAttachmentInputStreamsInterceptor extends AbstractPhaseInterceptor<Message> {

    @Autowired
    private CxfAttachmentCleanupService attachmentCleanupService;

    public CloseAttachmentInputStreamsInterceptor() {
        super(Phase.PREPARE_SEND_ENDING);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Exchange exchange = message.getExchange();
        closeAttachmentInputStreams(exchange);
    }

    @Override
    public void handleFault(Message message) {
        Exchange exchange = message.getExchange();
        closeAttachmentInputStreams(exchange);
    }

    private void closeAttachmentInputStreams(Exchange exchange) {
        if (exchange.getOutMessage() != null) {

            attachmentCleanupService.cleanCxfAttachments(exchange.getOutMessage().getAttachments());
        }
    }

}
