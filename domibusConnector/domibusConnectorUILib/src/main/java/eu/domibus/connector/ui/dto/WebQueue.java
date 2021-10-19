package eu.domibus.connector.ui.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.jms.Message;
import java.util.List;

@Data
public class WebQueue {
    private String name;
    private List<Message> messages;
    private List<Message> dlqMessages;
    private int msgsOnQueue;
    private int msgsOnDlq;

    public String getName() {
        return StringUtils.capitalize(name).replace("/([A-Z])/g", "$1").trim();
    }
}
