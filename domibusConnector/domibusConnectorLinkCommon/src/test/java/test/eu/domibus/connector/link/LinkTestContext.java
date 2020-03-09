package test.eu.domibus.connector.link;

import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.service.SubmitToConnector;
import eu.domibus.connector.controller.service.TransportStatusService;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.link.LinkPackage;
import eu.domibus.connector.link.service.DCLinkPluginConfiguration;
import eu.domibus.connector.link.service.DCLinkPluginConfigurationProperties;
import eu.domibus.connector.persistence.dao.DomibusConnectorLinkConfigurationDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, })
@Import(LinkTestContext.LinkServiceContext.class)
public class LinkTestContext {

    private static final Logger LOGGER = LogManager.getLogger(LinkTestContext.class);

    @Configuration
    @ComponentScan(basePackageClasses = DCLinkPluginConfiguration.class)
    public static class LinkServiceContext {

    }

    @Bean
    @ConditionalOnMissingBean
    public TransportStatusService transportStatusService() {
        TransportStatusService mock = Mockito.mock(TransportStatusService.class);

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

//    @Bean
//    @Qualifier(SUBMIT_TO_CONNECTOR_QUEUE)
//    public BlockingQueue<DomibusConnectorMessage> toConnectorSubmittedMessages() {
//        return new LinkedBlockingDeque<>(90);
//    }

    public static final BlockingQueue<DomibusConnectorMessage> TO_CONNECTOR_SUBMITTED_MESSAGES = new LinkedBlockingDeque<>(90);

    public static class SubmitToConnectorQueuImpl implements SubmitToConnector {

//        @Autowired
//        @Qualifier(SUBMIT_TO_CONNECTOR_QUEUE)
//        public BlockingQueue<DomibusConnectorMessage> toConnectorSubmittedMessages;

        @Override
        public void submitToConnector(DomibusConnectorMessage message, DomibusConnectorLinkPartner linkPartner) throws DomibusConnectorSubmitToLinkException {

            LOGGER.info("Adding message [{}] to submitToConnector [{}] Queue", message, TO_CONNECTOR_SUBMITTED_MESSAGES);
            try {
                TO_CONNECTOR_SUBMITTED_MESSAGES.put(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
