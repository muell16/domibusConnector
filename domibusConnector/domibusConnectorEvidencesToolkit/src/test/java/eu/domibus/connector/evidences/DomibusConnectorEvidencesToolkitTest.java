package eu.domibus.connector.evidences;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.annotation.Resource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.domibus.connector.domain.Message;
import eu.domibus.connector.domain.MessageConfirmation;
import eu.domibus.connector.domain.MessageContent;
import eu.domibus.connector.domain.MessageDetails;
import eu.domibus.connector.domain.enums.EvidenceType;
import eu.domibus.connector.evidences.exception.DomibusConnectorEvidencesToolkitException;
import eu.domibus.connector.evidences.type.RejectionReason;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test/context/testContext.xml")
public class DomibusConnectorEvidencesToolkitTest {

    private static Logger LOG = LoggerFactory.getLogger(DomibusConnectorEvidencesToolkitTest.class);

    @Resource
    private DomibusConnectorEvidencesToolkit evidencesToolkit;
   
    @Test
    public void testCreateSubmissionAcceptance() {
        LOG.info("Started testCreateSubmissionAcceptance");

        Message message = buildTestMessage();

        try {
        	
        	MessageConfirmation confirmation = evidencesToolkit.createEvidence(EvidenceType.SUBMISSION_ACCEPTANCE, message, null, null);
        	Assert.assertNotNull(confirmation);
        	String evidencePretty = prettyPrint(confirmation.getEvidence());
//            evidencesToolkit.createSubmissionAcceptance(message);
//            Assert.assertNotNull(message.getConfirmations());
//            Assert.assertEquals(1, message.getConfirmations().size());
//            String evidencePretty = prettyPrint(message.getConfirmations().get(0).getEvidence());
            LOG.info(evidencePretty);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
            Assert.fail();
        } catch (TransformerException e) {
            e.printStackTrace();
            Assert.fail();
        }
        LOG.info("Finished testCreateSubmissionAcceptance");
    }

    @Test
    public void testCreateSubmissionRejection() {
        LOG.info("Started testCreateSubmissionRejection");

        Message message = buildTestMessage();

        try {
        	MessageConfirmation confirmation = evidencesToolkit.createEvidence(EvidenceType.SUBMISSION_REJECTION, message, RejectionReason.OTHER, null);
        	Assert.assertNotNull(confirmation);
        	String evidencePretty = prettyPrint(confirmation.getEvidence());
//            evidencesToolkit.createSubmissionRejection(RejectionReason.OTHER, message);
//            Assert.assertNotNull(message.getConfirmations());
//            Assert.assertEquals(1, message.getConfirmations().size());
//            String evidencePretty = prettyPrint(message.getConfirmations().get(0).getEvidence());
            LOG.info(evidencePretty);
        } catch (DomibusConnectorEvidencesToolkitException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
            Assert.fail();
        } catch (TransformerException e) {
            e.printStackTrace();
            Assert.fail();
        }
        LOG.info("Finished testCreateSubmissionRejection");
    }

    private Message buildTestMessage() {
        MessageDetails details = new MessageDetails();
        details.setNationalMessageId("nationalMessageId1");
        details.setOriginalSender("someSenderAddress");
        details.setFinalRecipient("someRecipientAddress");

        MessageContent content = new MessageContent();
        content.setNationalXmlContent(new String("originalMessage").getBytes());
        content.setPdfDocument(new String("originalMessage").getBytes());

        Message message = new Message(details, content);

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
