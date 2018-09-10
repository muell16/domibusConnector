package eu.domibus.connector.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("")
public class RedirectController {

    public RedirectView redirectWithUsingRedirectView() {
        return new RedirectView("domibusConnector/");
    }

}
