package test.eu.domibus.connector.link;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.DomibusConnectorMessageIdGenerator;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService;
import eu.domibus.connector.link.common.MerlinPropertiesFactory;
import eu.domibus.connector.link.service.DCLinkPluginConfiguration;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkConfigurationDao;
import eu.domibus.connector.persistence.service.DomibusConnectorMessagePersistenceService;
import eu.domibus.connector.persistence.service.testutil.LargeFilePersistenceServiceMemoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, })
@Import(LinkTestContext.LinkServiceContext.class)
public class LinkTestContext {

    private static final Logger LOGGER = LogManager.getLogger(LinkTestContext.class);

    @Bean
    public MerlinPropertiesFactory merlinPropertiesFactory() {
        return new MerlinPropertiesFactory();
    }

    @Configuration
    @ComponentScan(basePackageClasses = DCLinkPluginConfiguration.class)
    public static class LinkServiceContext {

    }

    @Bean
    @ConditionalOnMissingBean
    DomibusConnectorMessageIdGenerator DomibusConnectorMessageIdGenerator() {
        return () -> "testcon_" + UUID.randomUUID().toString();
    }

    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorMessagePersistenceService domibusConnectorMessagePersistenceService() {
        return Mockito.mock(DomibusConnectorMessagePersistenceService.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public LargeFilePersistenceServiceMemoryImpl largeFilePersistenceServiceMemoryImpl() {
        return new LargeFilePersistenceServiceMemoryImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public DomibusConnectorDomainMessageTransformerService transformerService() {
        return new DomibusConnectorDomainMessageTransformerService(largeFilePersistenceServiceMemoryImpl());
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
//    @ConditionalOnMissingBean(value = DomibusConnectorLinkConfigurationDao.class, search = SearchStrategy.ALL)
//    public DomibusConnectorLinkConfigurationDao domibusConnectorLinkConfigurationDao() {
//        DomibusConnectorLinkConfigurationDao dao = Mockito.mock(DomibusConnectorLinkConfigurationDao.class);
//        return dao;
//    }

    @Bean
    @Primary
    public SubmitToConnector submitToConnector() {
        return new SubmitToConnectorQueuImpl();
    }


    public static final String SUBMIT_TO_CONNECTOR_QUEUE = "submitToConnector";

    @Bean
    @Qualifier(SUBMIT_TO_CONNECTOR_QUEUE)
    public BlockingQueue<DomibusConnectorMessage> toConnectorSubmittedMessages() {
        return new LinkedBlockingDeque<>(90);
    }

    public static class SubmitToConnectorQueuImpl implements SubmitToConnector {

        @Autowired
        @Qualifier(SUBMIT_TO_CONNECTOR_QUEUE)
        public BlockingQueue<DomibusConnectorMessage> toConnectorSubmittedMessages;

        @Override
        public void submitToConnector(DomibusConnectorMessage message, DomibusConnectorLinkPartner linkPartner) throws DomibusConnectorSubmitToLinkException {

            LOGGER.info("Adding message [{}] to submitToConnector [{}] Queue", message, toConnectorSubmittedMessages);
            try {
                toConnectorSubmittedMessages.put(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
