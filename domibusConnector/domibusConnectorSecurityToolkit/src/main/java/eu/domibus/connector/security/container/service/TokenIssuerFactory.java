package eu.domibus.connector.security.container.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.ecodex.dss.model.token.TokenIssuer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenIssuerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenIssuerFactory.class);

//    @Value("${token.issuer.country:#{null}}")
//    String country;
//
//    @Value("${token.issuer.service.provider:#{null}}")
//    String serviceProvider;
//
//    @Value("${token.issuer.aes.value:#{null}}")
//    AdvancedSystemType advancedElectronicSystem;

    @Autowired
    TokenIssuerFactoryProperties tokenIssuerFactoryProperties;

    public TokenIssuer getTokenIssuer(DomibusConnectorMessage message) {
        TokenIssuer tokenIssuer = new TokenIssuer();
        tokenIssuer.setCountry(tokenIssuerFactoryProperties.getCountry());
        tokenIssuer.setServiceProvider(tokenIssuerFactoryProperties.getServiceProvider());
        tokenIssuer.setAdvancedElectronicSystem(tokenIssuerFactoryProperties.getAdvancedElectronicSystemType());
        LOGGER.debug("#getTokenIssuer: TokenIssuer initialized with country [{}], serviceProvide [{}] and advancedElectronicSystem [{}], returned tokenIssuer [{}]",
                tokenIssuerFactoryProperties.getCountry(), tokenIssuerFactoryProperties.getServiceProvider(), tokenIssuerFactoryProperties.getAdvancedElectronicSystemType(), tokenIssuer);

        return tokenIssuer;
    }

//    @PostConstruct
//    public void checkValues() {
//        if (this.country == null) {
//            throw new IllegalArgumentException("Check property 'token.issuer.country' is not allowed to be null!");
//        }
//        String[] countries = Locale.getISOCountries();
//        if (!ArrayUtils.contains(countries, this.country)) {
//            throw new IllegalArgumentException("Check that your property 'token.issuer.country' matches a ISO 3166 two-letter country codes!");
//        }
//        if (this.serviceProvider == null) {
//            throw new IllegalArgumentException("Check property 'token.issuer.service.provider' is not allowed to be null!");
//        }
//        if (this.advancedElectronicSystem == null) {
//            throw new IllegalArgumentException("Check property 'token.issuer.aes.value' is not null!");
//        }
//    }


}
