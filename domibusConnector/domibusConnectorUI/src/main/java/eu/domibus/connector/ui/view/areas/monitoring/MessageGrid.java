package eu.domibus.connector.ui.view.areas.monitoring;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.ui.controller.QueueController;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.List;

public class MessageGrid extends Grid<Message> {

    final QueueController queueController;

    public MessageGrid(QueueController queueController) {
        super();
        this.queueController = queueController;

        this.setWidth("90%");
        this.setHeightByRows(true);

        final Button delete = new Button("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        // todo add behovor

        final Button restore = new Button("Restore");
        restore.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        // todo add behovor

        addColumn(this::getJMSMessageID).setHeader("Message ID (JMS ID)").setWidth("25%");
        addColumn(this::getConnectorId).setHeader("Connector ID").setWidth("25%");
        addComponentColumn(webMsg -> restore).setHeader("Restore Message and try to reprocess").setWidth("25%");
        addComponentColumn(webMsg -> delete).setHeader("Delete Message forever").setWidth("25%");
    }

    private String getJMSMessageID(Message message) {
        String result = null;
        try {
            result = message.getJMSMessageID();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setMessages(List<Message> msgs) {
        this.setItems(msgs);
    }

    private String getConnectorId(Message msg) {
        String result = null;
        try {
            final DomibusConnectorMessage domibusConnectorMessage = (DomibusConnectorMessage) queueController.getConverter().fromMessage(msg);
            result = domibusConnectorMessage.getConnectorMessageId().getConnectorMessageId();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return result;
    }
}