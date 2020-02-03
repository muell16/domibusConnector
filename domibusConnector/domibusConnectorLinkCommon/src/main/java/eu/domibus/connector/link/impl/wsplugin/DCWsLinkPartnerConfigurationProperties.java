package eu.domibus.connector.link.impl.wsplugin;

import eu.ecodex.utils.configuration.api.annotation.ConfigurationDescription;
import eu.ecodex.utils.configuration.api.annotation.ConfigurationLabel;
import org.springframework.boot.context.properties.ConfigurationProperties;


public class DCWsLinkPartnerConfigurationProperties {

    @ConfigurationLabel("The address where the link partner is available")
    @ConfigurationDescription("Configure here the address where the remote soap service is listening")
    private String pushAddress = "";

    @ConfigurationLabel("Encryption Alias")
    @ConfigurationDescription("The alias of the certificate of the link partner. So the connector can find \n" +
            "the correct certificate and us this public key to encrpyt the message")
    private String encryptionAlias = "";

    public String getPushAddress() {
        return pushAddress;
    }

    public void setPushAddress(String pushAddress) {
        this.pushAddress = pushAddress;
    }

    public String getEncryptionAlias() {
        return encryptionAlias;
    }

    public void setEncryptionAlias(String encryptionAlias) {
        this.encryptionAlias = encryptionAlias;
    }
}
