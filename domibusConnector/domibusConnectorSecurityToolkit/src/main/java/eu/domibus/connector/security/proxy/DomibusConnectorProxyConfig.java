package eu.domibus.connector.security.proxy;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.europa.esig.dss.client.http.proxy.ProxyConfig;
import eu.europa.esig.dss.client.http.proxy.ProxyProperties;

@Component("domibusConnectorProxyConfig")
public class DomibusConnectorProxyConfig extends ProxyConfig implements InitializingBean {

	@Value("${http.proxy.enabled}")
	boolean httpProxyEnabled;
	
	@Value("${http.proxy.host:null}")
	String httpProxyHost;
	
	@Value("${http.proxy.port:0}")
	String httpProxyPort;
	
	@Value("${http.proxy.user:null}")
	String httpProxyUser;
	
	@Value("${http.proxy.password:null}")
	String httpProxyPassword;
	
	@Value("${https.proxy.enabled}")
	boolean httpsProxyEnabled;
	
	@Value("${https.proxy.host:null}")
	String httpsProxyHost;
	
	@Value("${https.proxy.port:0}")
	String httpsProxyPort;
	
	@Value("${https.proxy.user:null}")
	String httpsProxyUser;
	
	@Value("${https.proxy.password:null}")
	String httpsProxyPassword;
    
    public DomibusConnectorProxyConfig() {
        
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		if(Boolean.valueOf(httpProxyEnabled)){
        	ProxyProperties httpProperties = new ProxyProperties();
        	httpProperties.setHost(httpProxyHost);
        	httpProperties.setPort(Integer.parseInt(httpProxyPort));
        	httpProperties.setUser(httpProxyUser);
        	httpProperties.setPassword(httpProxyPassword);
        	
        	setHttpProperties(httpProperties);
        }
        
        if(Boolean.valueOf(httpsProxyEnabled)){
        	ProxyProperties httpsProperties = new ProxyProperties();
        	httpsProperties.setHost(httpsProxyHost);
        	httpsProperties.setPort(Integer.parseInt(httpsProxyPort));
        	httpsProperties.setUser(httpsProxyUser);
        	httpsProperties.setPassword(httpsProxyPassword);
        	
        	setHttpsProperties(httpsProperties);
        }
	}

}
