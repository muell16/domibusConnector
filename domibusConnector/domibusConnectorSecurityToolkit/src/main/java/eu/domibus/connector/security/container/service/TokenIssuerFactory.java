package eu.domibus.connector.security.container.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.model.token.TokenIssuer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenIssuerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenIssuerFactory.class);

    @Value("${token.issuer.country:#{null}}")
    String country;

    @Value("${token.issuer.service.provider:#{null}}")
    String serviceProvider;

    @Value("${token.issuer.aes.value:#{null}}")
    AdvancedSystemType advancedElectronicSystem;

    public TokenIssuer getTokenIssuer(DomibusConnectorMessage message) {
        TokenIssuer tokenIssuer = new TokenIssuer();
        tokenIssuer.setCountry(country);
        tokenIssuer.setServiceProvider(serviceProvider);
        tokenIssuer.setAdvancedElectronicSystem(advancedElectronicSystem);
        LOGGER.debug("#getTokenIssuer: TokenIssuer initialized with country [{}], serviceProvide [{}] and advancedElectronicSystem [{}], returned tokenIssuer [{}]",
                country, serviceProvider, advancedElectronicSystem, tokenIssuer);

        return tokenIssuer;
    }


}
