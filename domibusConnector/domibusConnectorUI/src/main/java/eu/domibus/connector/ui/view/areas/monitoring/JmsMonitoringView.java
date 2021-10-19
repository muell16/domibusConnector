package eu.domibus.connector.ui.view.areas.monitoring;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.ui.controller.QueueController;
import eu.domibus.connector.ui.dto.WebQueue;
import eu.domibus.connector.ui.view.areas.configuration.TabMetadata;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = JmsMonitoringView.ROUTE, layout = MonitoringLayout.class)
@Order(1)
@TabMetadata(title = "Jms Queues", tabGroup = MonitoringLayout.TAB_GROUP_NAME)
public class JmsMonitoringView extends VerticalLayout implements AfterNavigationObserver {
    public static final String ROUTE = "queues";

    private final QueueController queueController;
    private QueueGrid queueGrid;

    public JmsMonitoringView(QueueController queueController) {
        this.queueController = queueController;
        queueGrid = new QueueGrid();
        queueGrid.setItemDetailsRenderer(createDetailsRenderer());
        final VerticalLayout gridLayoutCotainer = new VerticalLayout(queueGrid);
        add(gridLayoutCotainer);
    }

    private ComponentRenderer<DetailsLayout, WebQueue> createDetailsRenderer() {
        return new ComponentRenderer<>(() -> new DetailsLayout(queueController),
                DetailsLayout::setData);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        queueGrid.setItems(queueController.getQueues());
    }
}
