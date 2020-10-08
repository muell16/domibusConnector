
package eu.domibus.connector.gateway.link.ws.spring;

//import eu.domibus.connector.configuration.annotation.ConfigurationDescription;
//import eu.domibus.connector.configuration.annotation.ConfigurationLabel;
import eu.domibus.connector.lib.spring.configuration.CxfTrustKeyStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreAndTrustStoreConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.validation.CheckKeyIsLoadableFromKeyStore;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static eu.domibus.connector.gateway.link.ws.spring.GatewayLinkWsContext.GW_LINK_WS_PROFILE;



/**
 * The Gateway Link Webservice Based Properties
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@CheckKeyIsLoadableFromKeyStore
@Component(GatewayLinkWsServiceProperties.BEAN_NAME)
@Profile(GW_LINK_WS_PROFILE)
@ConfigurationProperties(prefix = GatewayLinkWsServiceProperties.PREFIX)
@Validated
@Valid
@PropertySource("classpath:eu/domibus/connector/gateway/link/ws/spring/gatewaylinkws-default.properties")
//@ConfigurationDescription("Contains all properties which are related to configure the connection to the gateway over webservices (soap)")
public class GatewayLinkWsServiceProperties extends CxfTrustKeyStoreConfigurationProperties {

    public static final String BEAN_NAME = "GatewayLinkWsServiceProperties";
    
    public static final String PREFIX = "connector.gatewaylink.ws";
    
    public static final String PULL_ENABLED_PROPERTY_NAME = "pullEnabled";

    /**
     * Defines the URL for submitting messages to the Gateway
     */
    @NotBlank
    private String submissionEndpointAddress;

    /**
     * defines the publishAddress for the deliver message service.
     *
     * The service url is defined relative to the url of the CXF-Servlet (usually configured under /service),
     * the defined url is appended. So the default url for pushing messages from domibus gateway to connector
     * will be ${SERVER_CONTEXT}/service/delivermessage
     *
     */
    @NotBlank
    private String publishAddress = "/domibusConnectorDeliveryWebservice";
    
    private boolean pullEnabled=false;
    
//    private String name = "DeliverMessage";

    /**
     * SSL Key Store configuration
     *
     * The SSL-Key Store holds the path to the keyStore and the keyStore password to access the private-key which is needed to establish the TLS connection
     * to the Gateway. The private key is used to authenticate against the Gateway.
     */
    @NestedConfigurationProperty
//    @ConfigurationDescription("TLS between GW - Connector")
    private KeyAndKeyStoreAndTrustStoreConfigurationProperties tls;


//    @Valid
//    @NestedConfigurationProperty
//    @NotNull
//    @ConfigurationDescription("CXF encryption, signing, certs connector - GW")
//    private CxfTrustKeyStoreConfigurationProperties cxf;


    @Valid
    @NestedConfigurationProperty
    @NotNull
//    @ConfigurationLabel("WS Policy for GW <-> Connector Link")
//    @ConfigurationDescription("This Property is used to define the location of the ws policy which is used for communication with the gateway")
    private Resource wsPolicy = new ClassPathResource("/wsdl/backend.policy.xml");

    public String getSubmissionEndpointAddress() {
        return submissionEndpointAddress;
    }

    public void setSubmissionEndpointAddress(String submissionEndpointAddress) {
        this.submissionEndpointAddress = submissionEndpointAddress;
    }

    public String getPublishAddress() {
        return publishAddress;
    }

    public void setPublishAddress(String publishAddress) {
        this.publishAddress = publishAddress;
    }

    public KeyAndKeyStoreAndTrustStoreConfigurationProperties getTls() {
        return tls;
    }

    public void setTls(KeyAndKeyStoreAndTrustStoreConfigurationProperties tls) {
        this.tls = tls;
    }

//    public CxfTrustKeyStoreConfigurationProperties getCxf() {
//        return cxf;
//    }
//
//    public void setCxf(CxfTrustKeyStoreConfigurationProperties cxf) {
//        this.cxf = cxf;
//    }

    public Resource getWsPolicy() {
        return wsPolicy;
    }

    public void setWsPolicy(Resource wsPolicy) {
        this.wsPolicy = wsPolicy;
    }

	public boolean isPullEnabled() {
		return pullEnabled;
	}

	public void setPullEnabled(boolean pullEnabled) {
		this.pullEnabled = pullEnabled;
	}
}
