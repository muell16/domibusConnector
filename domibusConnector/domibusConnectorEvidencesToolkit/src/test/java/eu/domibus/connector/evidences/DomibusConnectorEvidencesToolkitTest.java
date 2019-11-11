package eu.domibus.connector.evidences;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.enums.DomibusConnectorRejectionReason;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.transformer.util.DomibusConnectorBigDataReferenceMemoryBacked;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.evidences.spring.DomibusConnectorEvidencesToolkitContext;
import eu.domibus.connector.evidences.spring.EvidencesToolkitConfigurationProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        DomibusConnectorEvidencesToolkitTest.DomibusConnectorEvidencesToolkitTestContext.class,
        DomibusConnectorEvidencesToolkitContext.class,
        EvidencesToolkitConfigurationProperties.class,
})
@TestPropertySource(locations = "classpath:test.properties")
@EnableConfigurationProperties
public class DomibusConnectorEvidencesToolkitTest {

    @Configuration
    static class DomibusConnectorEvidencesToolkitTestContext {

        @Bean
        public static PropertySourcesPlaceholderConfigurer
        propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    private static Logger LOG = LoggerFactory.getLogger(DomibusConnectorEvidencesToolkitTest.class);

    @Autowired
    private DomibusConnectorEvidencesToolkit evidencesToolkit;

    @Autowired
    private EvidencesToolkitConfigurationProperties evidencesToolkitConfigurationProperties;


    @Test
    public void testCreateSubmissionAcceptance() throws DomibusConnectorEvidencesToolkitException, TransformerException {
        LOG.info("Started testCreateSubmissionAcceptance");

        DomibusConnectorMessage message = buildTestMessage();

        assertThat(evidencesToolkit).as("evidences toolkit must be init!").isNotNull();
        assertThat(message).as("message must not be null!").isNotNull();

        DomibusConnectorMessageConfirmation confirmation = evidencesToolkit.createEvidence(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE, message, null, null);
        Assertions.assertNotNull(confirmation);
        String evidencePretty = prettyPrint(confirmation.getEvidence());
        LOG.info(evidencePretty);

        LOG.info("Finished testCreateSubmissionAcceptance");
    }

    @Test
    public void testCreateSubmissionRejection() {
        LOG.info("Started testCreateSubmissionRejection");

        DomibusConnectorMessage message = buildTestMessage();

        try {
            DomibusConnectorMessageConfirmation confirmation = evidencesToolkit.createEvidence(
                    DomibusConnectorEvidenceType.SUBMISSION_REJECTION,
                    message,
                    DomibusConnectorRejectionReason.OTHER, null);
            Assertions.assertNotNull(confirmation);
            String evidencePretty = prettyPrint(confirmation.getEvidence());
            LOG.info(evidencePretty);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            e.printStackTrace();
            Assertions.fail();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
            Assertions.fail();
        } catch (TransformerException e) {
            e.printStackTrace();
            Assertions.fail();
        }
        LOG.info("Finished testCreateSubmissionRejection");
    }

    private DomibusConnectorMessage buildTestMessage() {
        DomibusConnectorMessageDetails details = new DomibusConnectorMessageDetails();
        details.setBackendMessageId("nationalMessageId1");
        details.setOriginalSender("someSenderAddress");
        details.setFinalRecipient("someRecipientAddress");

        DomibusConnectorMessageContent content = new DomibusConnectorMessageContent();

        DomibusConnectorBigDataReferenceMemoryBacked ref = new DomibusConnectorBigDataReferenceMemoryBacked("originalMessage".getBytes());

        DomibusConnectorMessageDocument document =
                new DomibusConnectorMessageDocument(ref, "documentName", null);

        content.setXmlContent("originalMessage".getBytes());
        content.setDocument(document);

        DomibusConnectorMessage message = new DomibusConnectorMessage(details, content);

        return message;
    }

    private String prettyPrint(byte[] input) throws TransformerFactoryConfigurationError, TransformerException {
        // Instantiate transformer input
        Source xmlInput = new StreamSource(new ByteArrayInputStream(input));
        StreamResult xmlOutput = new StreamResult(new StringWriter());

        // Configure transformer
        Transformer transformer = TransformerFactory.newInstance().newTransformer(); // An
        // identity
        // transformer
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "testing.dtd");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(xmlInput, xmlOutput);

        return xmlOutput.getWriter().toString();
    }

}
