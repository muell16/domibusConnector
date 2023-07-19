package eu.domibus.connector.controller.queues;


import eu.domibus.connector.controller.exception.DomibusConnectorSubmitToLinkException;
import eu.domibus.connector.controller.queues.listener.ToLinkPartnerListener;
import eu.domibus.connector.controller.queues.producer.ToLinkQueue;
import eu.domibus.connector.controller.service.SubmitToLinkService;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {SubmitToPartnerITCase.TestContext.class}, properties = {"spring.liquibase.enabled=false"})
@ActiveProfiles({"test", "jms-test"})
@DirtiesContext
public class SubmitToPartnerITCase {

    @MockBean
    SubmitToLinkService submitToLinkService;

    @Autowired
    ToLinkPartnerListener toLinkPartnerListener;

    @Autowired
    ToLinkQueue toLinkQueue;

    @SpringBootApplication(scanBasePackages = "foo.bar")
    @Import({JmsConfiguration.class, ToLinkPartnerListener.class, ToLinkQueue.class})
    public static class TestContext {

    }

    @Test
    public void contextLoads() {

        assertThat(toLinkPartnerListener).isNotNull();



        final DomibusConnectorMessage simpleTestMessage = DomainEntityCreator.createEpoMessage();
        simpleTestMessage.setMessageLaneId(DomibusConnectorBusinessDomain.getDefaultMessageLaneId());

        Mockito.doThrow(new DomibusConnectorSubmitToLinkException(simpleTestMessage, "test error"))
                .when(submitToLinkService).submitToLink(any());

        toLinkQueue.putOnQueue(simpleTestMessage);

        Mockito.verify(submitToLinkService, Mockito.times(6)).submitToLink(any());
    }

}
