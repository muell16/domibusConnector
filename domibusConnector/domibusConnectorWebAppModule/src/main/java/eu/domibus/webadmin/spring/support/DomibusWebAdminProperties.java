
package eu.domibus.webadmin.spring.support;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

/**
 * Property POJO to support configuration metadata: {@link https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html}
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@ConfigurationProperties(prefix="webadmin")
@Component
public class DomibusWebAdminProperties {

    private boolean debug = false;
    
    private boolean enabled = true;

    private String servletPath = "/webadmin/";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    
    
}
