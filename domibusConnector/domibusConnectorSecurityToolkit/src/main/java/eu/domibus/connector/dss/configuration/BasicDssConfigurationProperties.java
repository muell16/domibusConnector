package eu.domibus.connector.dss.configuration;

import eu.europa.esig.dss.service.http.proxy.ProxyProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = BasicDssConfigurationProperties.PREFIX)
@Validated
public class BasicDssConfigurationProperties {

    public static final String PREFIX = "connector.dss";

    private ProxyProperties httpsProxy;
    private ProxyProperties httpProxy;

    private Map<String, @Valid Tsp> timeStampServers = new HashMap<>();

    private Map<String, @Valid TrustListSourceConfigurationProperties> trustSource = new HashMap<>();

    public ProxyProperties getHttpsProxy() {
        return httpsProxy;
    }

    public void setHttpsProxy(ProxyProperties httpsProxy) {
        this.httpsProxy = httpsProxy;
    }

    public ProxyProperties getHttpProxy() {
        return httpProxy;
    }

    public void setHttpProxy(ProxyProperties httpProxy) {
        this.httpProxy = httpProxy;
    }

    public Map<String, Tsp> getTimeStampServers() {
        return timeStampServers;
    }

    public void setTimeStampServers(Map<String, Tsp> timeStampServers) {
        this.timeStampServers = timeStampServers;
    }

    @Valid
    public static class Tsp {

        @Pattern(regexp = "^https:\\/\\/", message = "Only https urls are allowed")
        private String url;

        private String policyOid;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPolicyOid() {
            return policyOid;
        }

        public void setPolicyOid(String policyOid) {
            this.policyOid = policyOid;
        }
    }

}
