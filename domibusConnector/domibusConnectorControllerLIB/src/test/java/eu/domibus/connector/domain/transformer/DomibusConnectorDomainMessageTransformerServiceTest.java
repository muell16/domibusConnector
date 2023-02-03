package eu.domibus.connector.domain.transformer;

import eu.domibus.connector.persistence.service.testutil.LargeFilePersistenceServicePassthroughImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;


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
// needs mocking if renabled
//        transformerService = new DomibusConnectorDomainMessageTransformerService(mockedLargeFilePersistenceService, messageProcessManager);
//        transformerService.messageIdThreadLocal.set(new DC5MessageId("id1"));
    }

    @AfterEach
    public void afterEach() {
        transformerService.messageIdThreadLocal.remove();
    }


}
