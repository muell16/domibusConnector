package eu.domibus.connector.controller.service;

import javax.jms.Message;
import java.util.List;
import java.util.Optional;

public interface HasManageableDlq extends PutOnQueue {
    List<Message> listAllMessagesWithinQueue();
    Optional<Message> fetchMsg(String jmsId);
}
