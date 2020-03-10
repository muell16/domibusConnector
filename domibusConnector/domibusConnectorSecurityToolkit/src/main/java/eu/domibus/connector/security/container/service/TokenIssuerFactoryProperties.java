package eu.domibus.connector.security.container.service;

import eu.ecodex.dss.model.token.AdvancedSystemType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "token.issuer")
@Component
public class TokenIssuerFactoryProperties {


    /**
     * The country where the connector is located
     *
     */
    String country = "";

    /**
     * Name of the service provider which is operating the
     * connector
     */
    String serviceProvider = "";

    /**
     * How is the outgoing message validated:
     * For Details read the e-Codex circle-of-trust agreement
     *  AUTHENTICATION_BASED: the connector is operating within a trusted system and if required the business document can be tracked
     *  to the person which is legally responsible for the document (can be don by a central document database, audit database,
     *  document creation managed in a blockchain database, ...)
     *  SIGNATURE_BASED: the business document itself is signed with a certificate, the connector itself needs to trust this certificate
     *
     * The result of the validation of the outgoing document leads to a generated TrustOK token, this token
     * is created as XML and PDF and is signed by the connector
     *
     */
    @NotNull
    AdvancedSystemType advancedElectronicSystemType;

    /**
     * If the AUTHENTICATION_BASED is used, the identity provider must be set
     *  the identity provider is the system which has authenticated the user
     */
    String identityProvider;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public AdvancedSystemType getAdvancedElectronicSystemType() {
        return advancedElectronicSystemType;
    }

    public void setAdvancedElectronicSystemType(AdvancedSystemType advancedElectronicSystemType) {
        this.advancedElectronicSystemType = advancedElectronicSystemType;
    }

    public String getIdentityProvider() {
        return identityProvider;
    }

    public void setIdentityProvider(String identityProvider) {
        this.identityProvider = identityProvider;
    }
}
