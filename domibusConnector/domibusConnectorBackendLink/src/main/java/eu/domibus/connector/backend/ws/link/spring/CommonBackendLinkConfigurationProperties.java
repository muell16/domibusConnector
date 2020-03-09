package eu.domibus.connector.backend.ws.link.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static eu.domibus.connector.backend.ws.link.spring.WSBackendLinkContextConfiguration.WS_BACKEND_LINK_PROFILE;

@Component
@Profile(WS_BACKEND_LINK_PROFILE)
@ConfigurationProperties(prefix = "connector.backend.common")
@Validated
public class CommonBackendLinkConfigurationProperties {

    @NotNull
    @Valid
    private Ping ping = new Ping();

    public Ping getPing() {
        return ping;
    }

    public void setPing(Ping ping) {
        this.ping = ping;
    }

    @Validated
    public static class Ping {
        @NotNull
        String action = "";
        @NotNull
        String service = "";

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }


}
