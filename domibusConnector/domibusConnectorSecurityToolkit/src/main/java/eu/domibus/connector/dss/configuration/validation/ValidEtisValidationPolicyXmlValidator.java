package eu.domibus.connector.dss.configuration.validation;

import eu.europa.esig.dss.policy.ValidationPolicy;
import eu.europa.esig.dss.policy.ValidationPolicyFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

public class ValidEtisValidationPolicyXmlValidator implements ConstraintValidator<ValidEtsiValidationPolicyXml, Resource> {

    private static final Logger LOGGER = LogManager.getLogger(ValidEtisValidationPolicyXmlValidator.class);

    @Override
    public void initialize(ValidEtsiValidationPolicyXml constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Resource value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            InputStream policyDataStream = value.getInputStream();
            ValidationPolicy validationPolicy = null;
            validationPolicy = ValidationPolicyFacade.newFacade().getValidationPolicy(policyDataStream);
            return true;
        } catch (IOException ioe) {
            LOGGER.warn("Error while loading resource", ioe);
            return false;
        } catch (XMLStreamException | JAXBException | SAXException e) {
//            e.printStackTrace();
            //TODO: error...
            LOGGER.warn("Parsing error during validation", e);
            return false;
        }
    }
}
