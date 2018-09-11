package eu.domibus.connector.web;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;


//@Controller
//@RequestMapping("/")

@Route("/")
public class RedirectController extends VerticalLayout implements BeforeEnterObserver {

//    public RedirectView redirectWithUsingRedirectView() {
//        return new RedirectView("domibusConnector/");
//    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        System.out.println("BEFORE ENTER.....");
        beforeEnterEvent.rerouteTo("/domibusConnector");
    }
}
