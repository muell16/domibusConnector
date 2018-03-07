package eu.domibus.connector.controller.test.util;



//TODO: NOT FINIHSED YET

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.builder.*;
import org.apache.log4j.lf5.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * Should load test messages from directory
 * store messages to directory
 *
 */
public class LoadStoreMessageFromPath {

    public static DomibusConnectorMessage loadMessageFrom(String basePath) throws IOException {
        DomibusConnectorMessageBuilder messageBuilder = DomibusConnectorMessageBuilder.createBuilder();

        Properties messageProps = new Properties();

        messageProps.load(loadRelativeResource(basePath, "message.properties"));

        //add message details
        messageBuilder.setMessageDetails(loadMessageDetailsFromProperties(messageProps));

        //add xml content
        DomibusConnectorMessageContent content = new DomibusConnectorMessageContent();
        content.setXmlContent(StreamUtils.getBytes(loadRelativeResource(basePath, "content.xml")));
        messageBuilder.setMessageContent(content);


        //add asic container attachment
        messageBuilder.addAttachment(DomibusConnectorMessageAttachmentBuilder.createBuilder()
                .setAttachment(loadRelativeResourceAsBigDataRef(basePath, "ASIC-S.asics"))
                .setIdentifier("ASIC-S")
                .build());

        //add token xml
        messageBuilder.addAttachment(DomibusConnectorMessageAttachmentBuilder.createBuilder()
                .setAttachment(loadRelativeResourceAsBigDataRef(basePath, "Token.xml"))
                .setIdentifier("tokenXML")
                .build());

        //add submission evidence
        messageBuilder.addConfirmation(DomibusConnectorMessageConfirmationBuilder.createBuilder()
                .setEvidence(loadRelativeResourceAsByteArray(basePath, "SUBMISSION_ACCEPTANCE.xml"))
                .setEvidenceType(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE)
                .build()
        );

        return messageBuilder.build();
    }

    private static DomibusConnectorBigDataReference loadRelativeResourceAsBigDataRef(String base, String relative) {
        try {
            InputStream inputStream = loadRelativeResource(base, relative);

            DomibusConnectorBigDataReferenceInMemory inMemory = new DomibusConnectorBigDataReferenceInMemory();
            inMemory.setInputStream(new ByteArrayInputStream(StreamUtils.getBytes(inputStream)));
            inMemory.setReadable(true);
            inMemory.setStorageIdReference(UUID.randomUUID().toString());
            return inMemory;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private static byte[] loadRelativeResourceAsByteArray(String base, String relative) {
        try {
            InputStream inputStream = loadRelativeResource(base, relative);
            return StreamUtils.getBytes(inputStream);
        } catch(IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }


    private static InputStream loadRelativeResource(String base, String relative) {
        String resource = base + "/" + relative;
        InputStream inputStream = LoadStoreMessageFromPath.class.getResourceAsStream(resource);
        if (inputStream == null) {
            throw new RuntimeException(String.format("Ressource %s could not be load!", resource));
        }
        return inputStream;
    }



    private static DomibusConnectorMessageDetails loadMessageDetailsFromProperties(Properties messageProps) {
        DomibusConnectorMessageDetails messageDetails = new DomibusConnectorMessageDetails();

        messageDetails.setAction(DomibusConnectorActionBuilder.createBuilder()
                .setAction(messageProps.getProperty("action"))
                .withDocumentRequired(false)
                .build()
        );

        messageDetails.setFromParty(DomibusConnectorPartyBuilder.createBuilder()
                .setPartyId(messageProps.getProperty("from.party.id"))
                .setRole(messageProps.getProperty("from.party.role"))
                .build()
        );

        messageDetails.setToParty(DomibusConnectorPartyBuilder.createBuilder()
                .setPartyId(messageProps.getProperty("to.party.id"))
                .setRole(messageProps.getProperty("to.party.role"))
                .build()
        );

        messageDetails.setService(DomibusConnectorServiceBuilder.createBuilder()
                .setService(messageProps.getProperty("service"))
                .build()
        );

        messageDetails.setConversationId(messageProps.getProperty("conversation.id"));

        messageDetails.setEbmsMessageId(messageProps.getProperty("ebms.message.id"));


        return messageDetails;
    }



}
