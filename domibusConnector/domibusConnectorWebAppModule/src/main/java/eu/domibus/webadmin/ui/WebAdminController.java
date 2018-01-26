
package eu.domibus.webadmin.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Controller
public class WebAdminController {


    @RequestMapping("/hallo")
    public String greeting() {        
        return "Hallo Welt";
    }
    
}
