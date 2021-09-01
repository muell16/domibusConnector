package eu.domibus.connector.web.view.areas.pmodes;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.domibus.connector.web.utils.RoleRequired;
import org.springframework.stereotype.Component;

@UIScope
@Component
@Route(value = PmodeOverview.ROUTE, layout = PmodeLayout.class)
@RoleRequired(role = "ADMIN")
public class PmodeOverview extends VerticalLayout implements BeforeEnterObserver {

    // Pmodelayout already has prefix "pmode"
    public static final String ROUTE = "";

    // Always redirect to Import
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.forwardTo(Import.class);
    }

}
