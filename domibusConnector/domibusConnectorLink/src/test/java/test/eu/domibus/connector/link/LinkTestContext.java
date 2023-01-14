package test.eu.domibus.connector.link;

import eu.domibus.connector.common.configuration.ConnectorConfigurationProperties;
import eu.domibus.connector.common.ConfigurationPropertyManagerServiceImpl;
import eu.domibus.connector.common.service.DCKeyStoreService;
import eu.domibus.connector.common.service.PropertyMapToBeanConverter;
import eu.domibus.connector.controller.exception.ErrorCode;
import eu.domibus.connector.controller.routing.DCRoutingRulesManager;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.ecodex.dc5.domain.DCBusinessDomainManager;
import eu.ecodex.dc5.message.model.DC5Message;
import eu.ecodex.dc5.message.model.DC5MessageId;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.lib.spring.configuration.validation.HelperMethods;
import eu.domibus.connector.link.common.MerlinPropertiesFactory;
import eu.domibus.connector.link.service.DCLinkPluginConfiguration;
import eu.ecodex.dc5.link.repository.DomibusConnectorLinkConfigurationDao;
import eu.ecodex.dc5.link.repository.DomibusConnectorLinkPartnerDao;
import eu.domibus.connector.persistence.service.DCLinkPersistenceService;
import eu.domibus.connector.persistence.service.DCMessagePersistenceService;
import eu.domibus.connector.persistence.service.LargeFilePersistenceService;
import eu.domibus.connector.persistence.service.testutil.LargeFilePersistenceServicePassthroughImpl;
import eu.ecodex.dc5.process.MessageProcessId;
import eu.ecodex.dc5.process.model.DC5MsgProcess;
import eu.ecodex.dc5.transport.repo.DC5TransportRequestRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.*;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, }, scanBasePackages = "eu.domibus.connector.link")
@Import({LinkTestContext.LinkServiceContext.class,
        HelperMethods.class,
//        DCKeyStoreService.class,
//        ConfigurationPropertyManagerServiceImpl.class,
//        PropertyMapToBeanConverter.class,

        //        DCBusinessDomainManagerImpl.class,
        ConnectorConfigurationProperties.class
})
@ComponentScan(basePackages = {"eu.domibus.connector.common"})
public class LinkTestContext {

    private static final Logger LOGGER = LogManager.getLogger(LinkTestContext.class);

//    @Bean
//    @ConditionalOnMissingBean
//    public MerlinPropertiesFactory merlinPropertiesFactory() {
//        return new MerlinPropertiesFactory();
//    }

//    @MockBean
//    SubmitToConnector submitToConnector;

    @Bean
    public SubmitToConnector submitToConnectorMockBean(@Qualifier(SUBMIT_TO_CONNECTOR_QUEUE) BlockingQueue<DC5Message> toConnectorMessages) {
        SubmitToConnector submitToConnector = new SubmitToConnector() {
            @Override
            public <T> ReceiveMessageFlowResult receiveMessage(T message, DC5TransformToDomain<T> transform) {
                DC5Message dc5Msg = transform.transform(message, DC5MsgProcess.builder().processId(MessageProcessId.ofRandom()).build());
                toConnectorMessages.add(dc5Msg);
                return ReceiveMessageFlowResult.getSuccess(dc5Msg);
            }
        };
        return submitToConnector;
    }

    @MockBean
    DC5TransportRequestRepo dc5TransportRequestRepo;

    @MockBean
    DCBusinessDomainManager dcBusinessDomainManager;

    @MockBean
    DomibusConnectorLinkPartnerDao dao;

//    @MockBean
//    DomibusConnectorBusinessDomainDao domainDao;

    @MockBean
    DCRoutingRulesManager rulesManager;

    @Configuration
    @ComponentScan(basePackageClasses = DCLinkPluginConfiguration.class)
    public static class LinkServiceContext {

    }

    @Bean
    @ConditionalOnMissingBean
    DCLinkPersistenceService dcLinkPersistenceService(DomibusConnectorLinkPartnerDao linkPartnerDao,
                                                      DomibusConnectorLinkConfigurationDao linkConfigurationDao) {
        return new DCLinkPersistenceService(linkPartnerDao, linkConfigurationDao);
    }

    @Bean
    @ConditionalOnMissingBean
    DomibusConnectorMessageIdGenerator DomibusConnectorMessageIdGenerator() {
        return () -> new DC5MessageId("testcon_" + UUID.randomUUID().toString());
    }

    @Bean
    @ConditionalOnMissingBean
    public DCMessagePersistenceService domibusConnectorMessagePersistenceService() {
        return Mockito.mock(DCMessagePersistenceService.class);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public LargeFileProviderMemoryImpl largeFilePersistenceServiceMemoryImpl() {
//        return new LargeFileProviderMemoryImpl();
//    }

    @Bean
    @ConditionalOnMissingBean
    public LargeFilePersistenceService largeFilePersistenceServicePassthroughImpl() {
        return new LargeFilePersistenceServicePassthroughImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorDomainMessageTransformerService transformerService(LargeFilePersistenceService largeFilePersistenceService) {
        return new DomibusConnectorDomainMessageTransformerService(largeFilePersistenceService);
    }

    @Bean
    @ConditionalOnMissingBean
    public TransportStateService transportStatusService() {
        TransportStateService mock = Mockito.mock(TransportStateService.class);

        return mock;
    }

    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorLinkConfigurationDao linkConfigurationDao() {
        return Mockito.mock(DomibusConnectorLinkConfigurationDao.class);
    }

//    @Bean
//    @Primary
//    public SubmitToConnector submitToConnector() {
//        return new SubmitToConnectorQueuImpl();
//    }


    public static final String SUBMIT_TO_CONNECTOR_QUEUE = "submitToConnector";

    @Bean
    @Qualifier(SUBMIT_TO_CONNECTOR_QUEUE)
    public BlockingQueue<DC5Message> toConnectorSubmittedMessages() {
        return new LinkedBlockingDeque<>(90);
    }

//    public static class SubmitToConnectorQueuImpl implements SubmitToConnector {
//
//        @Autowired
//        @Qualifier(SUBMIT_TO_CONNECTOR_QUEUE)
//        public BlockingQueue<DC5Message> toConnectorSubmittedMessages;
//
//        @Override
//        public void submitToConnector(DC5Message message, DomibusConnectorLinkPartner.LinkPartnerName linkPartner, LinkType linkType) throws DomibusConnectorSubmitToLinkException {
//
//            LOGGER.info("Adding message [{}] to submitToConnector [{}] Queue", message, toConnectorSubmittedMessages);
//            try {
//                toConnectorSubmittedMessages.put(message);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }



}
