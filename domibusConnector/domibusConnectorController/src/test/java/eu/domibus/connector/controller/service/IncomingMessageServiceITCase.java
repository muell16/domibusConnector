/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.controller.service;

import eu.domibus.connector.controller.DomibusConnectorIncomingController;
import eu.domibus.connector.controller.service.IncomingMessageServiceITCase.TestConfiguration;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorMessageContent;
import eu.domibus.connector.domain.model.DomibusConnectorMessageDetails;
import eu.domibus.connector.domain.model.builder.DomibusConnectorActionBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageAttachmentBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorMessageBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorPartyBuilder;
import eu.domibus.connector.domain.model.builder.DomibusConnectorServiceBuilder;
import eu.domibus.connector.persistence.service.DomibusConnectorPersistenceService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.lf5.util.StreamUtils;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
public class IncomingMessageServiceITCase {

    
    @SpringBootApplication(scanBasePackages = {"eu.domibus.connector"})
    static class TestConfiguration {

        @Bean
        public static PropertySourcesPlaceholderConfigurer
                propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }
    
    @Autowired
    ReceiveMessageFromGwService rcvMessageFromGwService;
    
    @MockBean
    SendMessageToGwService sendMessageToGwService;
    
    @MockBean
    SendMessageToBackendService sendMessageToBackendService;
    
//    @MockBean
//    DomibusConnectorPersistenceService persistenceService;
    
    @Autowired
    DomibusConnectorIncomingController domibusConnectorController;
    
    @Before
    public void setUp() {
        
    }
    
    
    @Test
    public void testReceiveMessageFromGw() throws IOException {
        
        //messageBuilder.setMessageDetails(loadMessageDetailsFromPropertyFile("/testmessages/msg1/message.properties"));
        DomibusConnectorMessage loadMessageFrom = loadMessageFrom("/testmessages/msg1/");
        
        assertThat(loadMessageFrom).isNotNull();
        
        rcvMessageFromGwService.receiveMessageFromGwService(loadMessageFrom);
        
    }
    
    private DomibusConnectorMessage loadMessageFrom(String basePath) throws IOException {
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
                .setAttachment(loadRelativeResourceAsByteArray(basePath, "ASIC-S.asics"))
                .setIdentifier("ASIC-S")
                .build());
        
        //add token xml
        messageBuilder.addAttachment(DomibusConnectorMessageAttachmentBuilder.createBuilder()
                .setAttachment(loadRelativeResourceAsByteArray(basePath, "Token.xml"))
                .setIdentifier("tokenXML")
                .build());
        
        //add submission evidence
        messageBuilder.addAttachment(DomibusConnectorMessageAttachmentBuilder.createBuilder()
                .setAttachment(loadRelativeResourceAsByteArray(basePath, "SUBMISSION_ACCEPTANCE.xml"))
                .setIdentifier("SUBMISSION_ACCEPTANCE")
                .build());
                        
        return messageBuilder.build();
    }
    
    private byte[] loadRelativeResourceAsByteArray(String base, String relative) {
        try {
            return StreamUtils.getBytes(loadRelativeResource(base, relative));
        } catch (IOException ioe) {
            throw new RuntimeException(String.format("Ressource %s could not be copied!", base + "/" + relative), ioe);
        }
    }
    
    private InputStream loadRelativeResource(String base, String relative) {
        String resource = base + "/" + relative;
        InputStream inputStream = getClass().getResourceAsStream(resource);
        if (inputStream == null) {
            throw new RuntimeException(String.format("Ressource %s could not be load!", resource));
        }
        return inputStream;
    }
    
    

    private DomibusConnectorMessageDetails loadMessageDetailsFromProperties(Properties messageProps) {        
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
