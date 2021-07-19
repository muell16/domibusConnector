package eu.domibus.connector.security.aes;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;

import eu.domibus.connector.security.container.service.TokenIssuerFactoryProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.ecodex.dss.model.token.AuthenticationInformation;
import eu.ecodex.dss.model.token.TechnicalTrustLevel;
import eu.ecodex.dss.model.token.TechnicalValidationResult;
import eu.ecodex.dss.model.token.TokenValidation;
import eu.ecodex.dss.model.token.ValidationVerification;
import org.springframework.stereotype.Component;

@Component
public class DomibusConnectorAESTokenValidationCreator {

    private static final Logger LOGGER = LogManager.getLogger(DomibusConnectorAESTokenValidationCreator.class);


    private final TokenIssuerFactoryProperties tokenIssuerFactoryProperties;

    public DomibusConnectorAESTokenValidationCreator(TokenIssuerFactoryProperties tokenIssuerFactoryProperties) {
        this.tokenIssuerFactoryProperties = tokenIssuerFactoryProperties;
    }

    TokenValidation createTokenValidation(DomibusConnectorMessage message) throws Exception {

        TokenValidation tValidation = new TokenValidation();

        final TechnicalValidationResult validationResult = new TechnicalValidationResult();
        final ValidationVerification validationVerification = new ValidationVerification();

        final AuthenticationInformation tokenAuthentication = new AuthenticationInformation();

        tokenAuthentication.setIdentityProvider(tokenIssuerFactoryProperties.getIdentityProvider());
        tokenAuthentication.setUsernameSynonym(message.getMessageDetails().getOriginalSender());
        tokenAuthentication.setTimeOfAuthentication(
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        
        validationVerification.setAuthenticationData(tokenAuthentication);

        tValidation.setTechnicalResult(validationResult);
        tValidation.setVerificationData(validationVerification);
        tValidation.setVerificationTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));

        try {
            // passed all the previous checks
            decide(TechnicalTrustLevel.SUCCESSFUL, "The signature is valid.", tValidation);

        } catch (Exception e) {
            LOGGER.warn("Exception occured during createTokenValidation", e);

            // Cannot generate the DSS validation report
            validationResult.setTrustLevel(TechnicalTrustLevel.FAIL);
            validationResult.setComment("An error occured, while validating the signature via DSS.");
            LOGGER.warn("b/o encountered exception: result determined to {}: {}", validationResult.getTrustLevel(),
                    validationResult.getComment());
        }
        return tValidation;
    }

    private void decide(final TechnicalTrustLevel level, final String comments, TokenValidation tValidation) {
        final TechnicalValidationResult r = tValidation.getTechnicalResult();
        r.setTrustLevel(level);
        r.setComment(comments);
        LOGGER.debug("result determined to {}: {}", r.getTrustLevel(), r.getComment());
    }

}
