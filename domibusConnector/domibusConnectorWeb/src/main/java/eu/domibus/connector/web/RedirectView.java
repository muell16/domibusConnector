package eu.domibus.connector.web;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.Route;
import eu.domibus.connector.web.view.MainView;

//TODO: get it to work, because instead in production mode a 404 will be shown on / url
//just redirect that to MainView.MAIN_VIEW_ROUTE

//@Route("")
public class RedirectView extends Div implements BeforeEnterListener  {

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        beforeEnterEvent.rerouteTo(MainView.MAIN_VIEW_ROUTE);
    }
}
