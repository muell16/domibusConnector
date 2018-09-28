
package eu.domibus.connector.gateway.link.spring;

import eu.domibus.connector.configuration.annotation.ConfigurationDescription;
import eu.domibus.connector.configuration.annotation.ConfigurationLabel;
import eu.domibus.connector.lib.spring.configuration.*;
import eu.domibus.connector.lib.spring.configuration.validation.CheckKeyIsLoadableFromKeyStore;
import eu.domibus.connector.lib.spring.configuration.validation.CheckStoreIsLoadable;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;

/**
 * The Gateway Link Webservice Based Properties
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component(GatewayLinkWsServiceProperties.BEAN_NAME)
@Profile("gwlink-ws")
@ConfigurationProperties(prefix = "connector.gatewaylink.ws")
@Validated
@Valid
public class GatewayLinkWsServiceProperties {

    public static final String BEAN_NAME = "GatewayLinkWsServiceProperties";

    /**
     * Defines the URL for submitting messages to the Gateway
     */
    @NotBlank
    private String submissionEndpointAddress;

    /**
     * defines the address for the deliver message service.
     *
     * The service url is defined relative to the url of the CXF-Servlet (usually configured under /service),
     * the defined url is appended. So the default url for pushing messages from domibus gateway to connector
     * will be ${SERVER_CONTEXT}/service/delivermessage
     *
     */
    @NotBlank
    private String address = "/domibusConnectorDeliveryWebservice";
    
//    private String name = "DeliverMessage";


//    connector.gatewaylink.ws.tls.key-store.password=12345
//    connector.gatewaylink.ws.tls.key-store.path=classpath:store.jks
//    connector.gatewaylink.ws.tls.private-key.password=12345
//
//    connector.gatewaylink.ws.tls.trust-store.path=classpath:store.jks
//    connector.gatewaylink.ws.tls.trust-store.password=1235


//    private KeyAndKeyStoreConfigurationProperties tls;


    @Valid
    @NestedConfigurationProperty
    @NotNull
    @ConfigurationDescription("TLS between GW - Connector")
    private KeyAndKeyStoreAndTrustStoreConfigurationProperties tls;

    @Valid
    @NestedConfigurationProperty
    @NotNull
    @ConfigurationDescription("CXF encryption, signing, certs connector - GW")
    private CxfTrustKeyStoreConfigurationProperties cxf;


    @Valid
    @NestedConfigurationProperty
    @NotNull
    @ConfigurationLabel("WS Policy for GW <-> Connector Link")
    @ConfigurationDescription("This Property is used to define the location of the ws policy which is used for communication with the gateway")
    private Resource wsPolicy = new ClassPathResource("/wsdl/backend.policy.xml");

    public String getSubmissionEndpointAddress() {
        return submissionEndpointAddress;
    }

    public void setSubmissionEndpointAddress(String submissionEndpointAddress) {
        this.submissionEndpointAddress = submissionEndpointAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public KeyAndKeyStoreAndTrustStoreConfigurationProperties getTls() {
        return tls;
    }

    public void setTls(KeyAndKeyStoreAndTrustStoreConfigurationProperties tls) {
        this.tls = tls;
    }

    public CxfTrustKeyStoreConfigurationProperties getCxf() {
        return cxf;
    }

    public void setCxf(CxfTrustKeyStoreConfigurationProperties cxf) {
        this.cxf = cxf;
    }

    public Resource getWsPolicy() {
        return wsPolicy;
    }

    public void setWsPolicy(Resource wsPolicy) {
        this.wsPolicy = wsPolicy;
    }
}
