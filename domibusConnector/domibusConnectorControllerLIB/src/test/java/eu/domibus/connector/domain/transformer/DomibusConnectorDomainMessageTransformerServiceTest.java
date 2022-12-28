package eu.domibus.connector.domain.transformer;

import eu.domibus.connector.persistence.service.testutil.LargeFilePersistenceServicePassthroughImpl;
import eu.ecodex.dc5.message.model.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


/**
 *
 *
 */
@Disabled
public class DomibusConnectorDomainMessageTransformerServiceTest {

    public DomibusConnectorDomainMessageTransformerServiceTest() {
    }


    DomibusConnectorDomainMessageTransformerService transformerService;
    LargeFilePersistenceServicePassthroughImpl mockedLargeFilePersistenceService;

    @BeforeEach
    public void init() {
        mockedLargeFilePersistenceService = new LargeFilePersistenceServicePassthroughImpl();

        transformerService = new DomibusConnectorDomainMessageTransformerService(mockedLargeFilePersistenceService);
        transformerService.messageIdThreadLocal.set(new DC5MessageId("id1"));
    }

    @AfterEach
    public void afterEach() {
        transformerService.messageIdThreadLocal.remove();
    }


}
