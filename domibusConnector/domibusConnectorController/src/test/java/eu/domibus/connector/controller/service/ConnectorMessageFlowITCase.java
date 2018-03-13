
package eu.domibus.connector.controller.service;


import eu.domibus.connector.controller.exception.DomibusConnectorGatewaySubmissionException;
import eu.domibus.connector.controller.test.util.DomibusConnectorBigDataReferenceInMemory;
import eu.domibus.connector.controller.test.util.ITCaseTestContext;
import eu.domibus.connector.controller.test.util.LoadStoreMessageFromPath;
import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.*;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import eu.domibus.connector.domain.model.builder.*;
import org.apache.log4j.lf5.util.StreamUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.core.io.ClassPathResource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.io.ByteArrayInputStream;

import org.springframework.test.context.jdbc.Sql;

/**
 *  Tests the message flow in the connector
 *      with persistence
 *      with security lib
 *      with evidence lib
 *
 *  WITHOUT
 *      backendlink
 *      gatewaylink
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ITCaseTestContext.class})
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = "/testdata.sql") //adds testdata to database like domibus-blue party
@ActiveProfiles("ITCaseTestContext")
public class ConnectorMessageFlowITCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorMessageFlowITCase.class);

    public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    private File testResultsFolder;
    private String testDateAsString;


    @Autowired
    @Qualifier(ITCaseTestContext.TO_GW_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    List<DomibusConnectorMessage> toGwDeliveredMessages;

    @Autowired
    @Qualifier(ITCaseTestContext.TO_BACKEND_DELIVERD_MESSAGES_LIST_BEAN_NAME)
    List<DomibusConnectorMessage> toBackendDeliveredMessages;
    
    @Autowired
    DomibusConnectorGatewayDeliveryService rcvMessageFromGwService;

    @Autowired
    DomibusConnectorBackendSubmissionService sendMessageToBackendService;

    
    @Before
    public void setUp() {
        String dir = System.getenv().getOrDefault(TEST_FILE_RESULTS_DIR_PROPERTY_NAME, "./target/testfileresults/");
        dir = dir + "/" + ConnectorMessageFlowITCase.class.getSimpleName();
        testResultsFolder = new File(dir);
        testResultsFolder.mkdirs();

        DateFormatter simpleDateFormatter = new DateFormatter();
        simpleDateFormatter.setPattern("yyyy-MM-dd-hh-mm");
        testDateAsString = simpleDateFormatter.print(new Date(), Locale.ENGLISH);

        //clear delivery lists
        toGwDeliveredMessages.clear();
        toBackendDeliveredMessages.clear();
    }
    
    
    @Test
    public void testReceiveMessageFromGw() throws IOException, DomibusConnectorGatewaySubmissionException, InterruptedException {
        DomibusConnectorMessage loadMessageFrom = LoadStoreMessageFromPath.loadMessageFrom(new ClassPathResource("/testmessages/msg2/"));
        
        assertThat(loadMessageFrom).isNotNull();
        loadMessageFrom.getMessageDetails().setFinalRecipient("final recipient");
        loadMessageFrom.getMessageDetails().setOriginalSender("original sender");

        LOGGER.info("message with confirmations: [{}]", loadMessageFrom.getMessageConfirmations());

        rcvMessageFromGwService.deliverMessageFromGateway(loadMessageFrom);

        Thread.sleep(2000); //TODO: replace with synchronized wait
        assertThat(toBackendDeliveredMessages).hasSize(1);
        //TODO: check database!
        //TODO: check jms queue!

    }
    

    
}
