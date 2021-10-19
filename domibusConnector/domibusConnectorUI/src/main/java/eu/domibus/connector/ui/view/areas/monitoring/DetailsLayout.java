package eu.domibus.connector.ui.view.areas.monitoring;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import eu.domibus.connector.ui.controller.QueueController;
import eu.domibus.connector.ui.dto.WebQueue;

public class DetailsLayout extends VerticalLayout {

    private final QueueController queueController;

    private MessageGrid msgsGrid;
    private MessageGrid dlqMsgsGrid;

    public DetailsLayout(QueueController queueController) {
        this.queueController = queueController;
        this.setWidth("100%");
        final Label msgsLabel = new Label("Messages on Queue");
        final Label dlqLabel = new Label("Messages on DLQ");
        msgsGrid = new MessageGrid(this.queueController);
        dlqMsgsGrid = new MessageGrid(this.queueController);
        add(msgsLabel, msgsGrid, dlqLabel, dlqMsgsGrid);
    }

    public void setData(WebQueue queue) {
        msgsGrid.setMessages(queue.getMessages());
        dlqMsgsGrid.setMessages(queue.getDlqMessages());
    }
}