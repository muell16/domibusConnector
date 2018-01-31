package eu.domibus.connector.security.aes;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.ecodex.dss.model.token.AuthenticationInformation;
import eu.ecodex.dss.model.token.TechnicalTrustLevel;
import eu.ecodex.dss.model.token.TechnicalValidationResult;
import eu.ecodex.dss.model.token.TokenValidation;
import eu.ecodex.dss.model.token.ValidationVerification;
import eu.ecodex.dss.util.LogDelegate;

@Component("domibusConnectorAESTokenValidationCreator")
public class DomibusConnectorAESTokenValidationCreator {

    private static final LogDelegate LOG = new LogDelegate(DomibusConnectorAESTokenValidationCreator.class);
    
    @Value("${identity.provider:null}")
    String identityProvider;

    TokenValidation createTokenValidation(DomibusConnectorMessage message) throws Exception {

        LOG.lInfo("creating result");

        TokenValidation tValidation = new TokenValidation();

        final TechnicalValidationResult validationResult = new TechnicalValidationResult();
        final ValidationVerification validationVerification = new ValidationVerification();

        final AuthenticationInformation tokenAuthentication = new AuthenticationInformation();

        tokenAuthentication.setIdentityProvider(identityProvider);
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
            LOG.mCause("run", e);

            // Cannot generate the DSS validation report
            validationResult.setTrustLevel(TechnicalTrustLevel.FAIL);
            validationResult.setComment("An error occured, while validating the signature via DSS.");
            LOG.lWarn("b/o encountered exception: result determined to {}: {}", validationResult.getTrustLevel(),
                    validationResult.getComment());
        }
        return tValidation;
    }

    private void decide(final TechnicalTrustLevel level, final String comments, TokenValidation tValidation) {
        final TechnicalValidationResult r = tValidation.getTechnicalResult();
        r.setTrustLevel(level);
        r.setComment(comments);
        LOG.lInfo("result determined to {}: {}", r.getTrustLevel(), r.getComment());

    }

}
