package eu.domibus.connector.security.aes;

import eu.ecodex.dc5.message.model.DC5Message;
import eu.domibus.connector.security.configuration.DCBusinessDocumentValidationConfigurationProperties;
import eu.ecodex.dss.service.ECodexTechnicalValidationService;

public interface DCAuthenticationBasedTechnicalValidationServiceFactory {

    ECodexTechnicalValidationService createTechnicalValidationService(DC5Message message, DCBusinessDocumentValidationConfigurationProperties.AuthenticationValidationConfigurationProperties config);

}
