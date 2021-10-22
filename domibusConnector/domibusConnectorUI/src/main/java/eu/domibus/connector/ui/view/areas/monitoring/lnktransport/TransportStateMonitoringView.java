package eu.domibus.connector.ui.view.areas.monitoring.lnktransport;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.persistence.service.TransportStepPersistenceService;
import eu.domibus.connector.ui.view.MainLayout;
import org.springframework.stereotype.Component;


@UIScope
@Component
@RoutePrefix(TransportStateMonitoringView.ROUTE_PREFIX)
@ParentLayout(MainLayout.class)
public class TransportStateMonitoringView extends VerticalLayout implements AfterNavigationObserver {

    public static final String ROUTE_PREFIX = "linktransport";

    private final TransportStateService transportStateService;
    private final TransportStepPersistenceService transportStepPersistenceService;

    public TransportStateMonitoringView(TransportStateService transportStateService, TransportStepPersistenceService transportStepPersistenceService) {
        this.transportStateService = transportStateService;
        this.transportStepPersistenceService = transportStepPersistenceService;
        initUI();
    }

    private void initUI() {

    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {
//        transportStepPersistenceService.getTransportStepByLastState(TransportState.PENDING_DOWNLOADED);
//        transportStepPersistenceService.findStepByLastState(TransportState.PENDING_DOWNLOADED);

    }



}
