package eu.domibus.connector.controller.test.util;



//TODO: NOT FINIHSED YET

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.model.builder.*;
import eu.domibus.connector.domain.transition.DomibusConnectorMessageConfirmationType;
import org.apache.log4j.lf5.util.StreamUtils;
import org.springframework.core.io.Resource;

import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Should load test messages from directory
 * store messages to directory
 *
 */
public class LoadStoreMessageFromPath {

    public static DomibusConnectorMessage loadMessageFrom(Resource resource) throws IOException {
        DomibusConnectorMessageBuilder messageBuilder = DomibusConnectorMessageBuilder.createBuilder();

        Properties messageProps = new Properties();

        Resource propertiesResource = resource.createRelative("message.properties");
        if (!propertiesResource.exists()) {
            throw new IOException("properties " + propertiesResource + " does not exist!");
        }

        messageProps.load(propertiesResource.getInputStream());

        messageBuilder.setMessageDetails(loadMessageDetailsFromProperties(messageProps));

        Resource contentResource = createRelativeResource(resource, messageProps.getProperty("message.content.xml"));
        if (contentResource != null && contentResource.exists()) {
            DomibusConnectorMessageContent content = new DomibusConnectorMessageContent();
            content.setXmlContent(StreamUtils.getBytes(contentResource.getInputStream()));

            messageBuilder.setMessageContent(content);
            //TODO: load document
        }

        messageBuilder.addAttachments(loadAttachments(resource, messageProps));
        messageBuilder.addConfirmations(loadConfirmations(resource, messageProps));

        String connid = messageProps.getProperty("message.connector-id", null);
        if (connid != null) {
            messageBuilder.setConnectorMessageId(connid);
        }

        return messageBuilder.build();

    }

    private static List<DomibusConnectorMessageConfirmation> loadConfirmations(Resource resource,  Properties messageProps) {
        String messageConfirmationsPrefix = "message.confirmation";
        return messageProps.stringPropertyNames()
                .stream()
                .sorted()
                .filter( (k) -> k.startsWith(messageConfirmationsPrefix))
                .map( (k) -> k.split("\\.")[2])
                .distinct()
                .map( (k) -> {


                        String evidenceFilePropertyName = String.format("%s.%s.%s", messageConfirmationsPrefix, k, "file");
                        String evidenceTypePropertyName = String.format("%s.%s.%s", messageConfirmationsPrefix, k, "type");

                        Resource resEvidenceFile = createRelativeResource(resource, messageProps.getProperty(evidenceFilePropertyName));
                        DomibusConnectorEvidenceType domibusConnectorEvidenceType = DomibusConnectorEvidenceType.valueOf(messageProps.getProperty(evidenceTypePropertyName));

                        DomibusConnectorMessageConfirmationBuilder builder = DomibusConnectorMessageConfirmationBuilder
                                .createBuilder()
                                .setEvidence(loadResourceAsByteArray(resEvidenceFile))
                                .setEvidenceType(domibusConnectorEvidenceType);

                        return builder.build();

                })
                .collect(Collectors.toList());
    }

    private static List<DomibusConnectorMessageAttachment> loadAttachments(Resource resource, Properties messageProps) {
        String messageAttachmentPrefix = "message.attachment";
        return messageProps.stringPropertyNames()
                .stream()
                .sorted()
                .filter( (k) -> k.startsWith(messageAttachmentPrefix) )
                .map( (k) -> k.split("\\.")[2])
                .distinct()
                .map( (k) -> {
                    try {
                        DomibusConnectorMessageAttachmentBuilder builder = DomibusConnectorMessageAttachmentBuilder.createBuilder();
                        String filePropertyName = String.format("%s.%s.%s", messageAttachmentPrefix, k, "file");
                        String identifierPropertyName = String.format("%s.%s.%s", messageAttachmentPrefix, k, "identifier");
                        Resource res = resource.createRelative(messageProps.getProperty(filePropertyName));
                        builder.setAttachment(loadResourceAsBigDataRef(res));
                        builder.setIdentifier(messageProps.getProperty(identifierPropertyName));

                        return builder.build();

                    } catch (IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                })
                .collect(Collectors.toList());
    }

    private static @Nullable Resource createRelativeResource(Resource res, @Nullable  String relativePath) {
        if (relativePath == null) {
            return null;
        }
        try {
            Resource contentResource = res.createRelative(relativePath);
            return contentResource;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

//    public static DomibusConnectorMessage loadMessageFrom(String basePath) throws IOException {
//        DomibusConnectorMessageBuilder messageBuilder = DomibusConnectorMessageBuilder.createBuilder();
//
//        Properties messageProps = new Properties();
//
//        messageProps.load(loadRelativeResource(basePath, "message.properties"));
//
//        //add message details
//        messageBuilder.setMessageDetails(loadMessageDetailsFromProperties(messageProps));
//
//        //add xml content
//        DomibusConnectorMessageContent content = new DomibusConnectorMessageContent();
//        content.setXmlContent(StreamUtils.getBytes(loadRelativeResource(basePath, "content.xml")));
//        messageBuilder.setMessageContent(content);
//
////
////        //add asic container attachment
////        messageBuilder.addAttachment(DomibusConnectorMessageAttachmentBuilder.createBuilder()
////                .setAttachment(loadResourceAsBigDataRef(basePath, "ASIC-S.asics"))
////                .setIdentifier("ASIC-S")
////                .build());
////
////        //add token xml
////        messageBuilder.addAttachment(DomibusConnectorMessageAttachmentBuilder.createBuilder()
////                .setAttachment(loadResourceAsBigDataRef(basePath, "Token.xml"))
////                .setIdentifier("tokenXML")
////                .build());
//
//        //add submission evidence
//        messageBuilder.addConfirmation(DomibusConnectorMessageConfirmationBuilder.createBuilder()
//                .setEvidence(loadRelativeResourceAsByteArray(basePath, "SUBMISSION_ACCEPTANCE.xml"))
//                .setEvidenceType(DomibusConnectorEvidenceType.SUBMISSION_ACCEPTANCE)
//                .build()
//        );
//
//        return messageBuilder.build();
//    }

    private static DomibusConnectorBigDataReference loadResourceAsBigDataRef(Resource resource) {
        try {
            InputStream inputStream = resource.getInputStream();

            DomibusConnectorBigDataReferenceInMemory inMemory = new DomibusConnectorBigDataReferenceInMemory();
            inMemory.setInputStream(new ByteArrayInputStream(StreamUtils.getBytes(inputStream)));
            inMemory.setReadable(true);
            inMemory.setStorageIdReference(UUID.randomUUID().toString());
            return inMemory;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


//    private static DomibusConnectorBigDataReference loadResourceAsBigDataRef(String base, String relative) {
//        try {
//            InputStream inputStream = loadRelativeResource(base, relative);
//
//            DomibusConnectorBigDataReferenceInMemory inMemory = new DomibusConnectorBigDataReferenceInMemory();
//            inMemory.setInputStream(new ByteArrayInputStream(StreamUtils.getBytes(inputStream)));
//            inMemory.setReadable(true);
//            inMemory.setStorageIdReference(UUID.randomUUID().toString());
//            return inMemory;
//        } catch (IOException ioe) {
//            throw new RuntimeException(ioe);
//        }
//    }

    private static byte[] loadResourceAsByteArray(Resource res) {
        try {
            InputStream inputStream = res.getInputStream();
            return StreamUtils.getBytes(inputStream);
        } catch(IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

//    private static byte[] loadRelativeResourceAsByteArray(String base, String relative) {
//        try {
//            InputStream inputStream = loadRelativeResource(base, relative);
//            return StreamUtils.getBytes(inputStream);
//        } catch(IOException ioe) {
//            throw new RuntimeException(ioe);
//        }
//    }

//
//    private static InputStream loadRelativeResource(String base, String relative) {
//        String resource = base + "/" + relative;
//        InputStream inputStream = LoadStoreMessageFromPath.class.getResourceAsStream(resource);
//        if (inputStream == null) {
//            throw new RuntimeException(String.format("Ressource %s could not be load!", resource));
//        }
//        return inputStream;
//    }



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
