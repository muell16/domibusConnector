package eu.domibus.connector.security.container.service;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.ecodex.dss.model.token.AdvancedSystemType;
import eu.ecodex.dss.model.token.TokenIssuer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TokenIssuerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenIssuerFactory.class);

    private final TokenIssuerFactoryProperties tokenIssuerFactoryProperties;

    public TokenIssuerFactory(TokenIssuerFactoryProperties tokenIssuerFactoryProperties) {
        this.tokenIssuerFactoryProperties = tokenIssuerFactoryProperties;
    }

    public TokenIssuer getTokenIssuer(DomibusConnectorMessage message) {
        TokenIssuer tokenIssuer = new TokenIssuer();
        tokenIssuer.setCountry(tokenIssuerFactoryProperties.getCountry());
        tokenIssuer.setServiceProvider(tokenIssuerFactoryProperties.getServiceProvider());
        tokenIssuer.setAdvancedElectronicSystem(this.getAdvancedElectronicSystemType(message));

        LOGGER.debug("#getTokenIssuer: TokenIssuer initialized with country [{}], serviceProvide [{}] and advancedElectronicSystem [{}], returned tokenIssuer [{}]",
                tokenIssuerFactoryProperties.getCountry(), tokenIssuerFactoryProperties.getServiceProvider(), tokenIssuerFactoryProperties.getAdvancedElectronicSystemType(), tokenIssuer);

        return tokenIssuer;
    }

    private final List<String> issuerServiceNames = Stream.of(AdvancedSystemType.values()).map(Enum::name).collect(Collectors.toList());


    public AdvancedSystemType getAdvancedElectronicSystemType(DomibusConnectorMessage message) {
        String validationServiceName = "";
        if (message.getDcMessageProcessSettings() != null && StringUtils.hasText(message.getDcMessageProcessSettings().getValidationServiceName())) {
            validationServiceName = message.getDcMessageProcessSettings().getValidationServiceName();
        }

        if (issuerServiceNames.contains(validationServiceName)) {
            LOGGER.debug("The validation service is determined from the validationServiceName of the message itself. It has been set by the backend application to [{}]", validationServiceName);
            return (AdvancedSystemType.valueOf(validationServiceName));
        } else {
            LOGGER.warn("The backend application has provided a illegal validationServiceName of [{}]. This setting will be ignored.", validationServiceName);
        }
        return tokenIssuerFactoryProperties.getAdvancedElectronicSystemType();

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
