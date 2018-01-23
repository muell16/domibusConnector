
package eu.domibus.connector.controller.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configures Controller Context
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Configuration
@Import(value = { QuartzContext.class })
public class ControllerContext {


}
