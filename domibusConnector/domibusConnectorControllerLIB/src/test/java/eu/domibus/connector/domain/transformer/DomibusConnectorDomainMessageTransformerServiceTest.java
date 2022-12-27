package eu.domibus.connector.domain.transformer;

import eu.domibus.connector.domain.enums.DomibusConnectorEvidenceType;
import eu.domibus.connector.domain.model.*;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.domain.transformer.DomibusConnectorDomainMessageTransformerService.CannotBeMappedToTransitionException;
import eu.domibus.connector.domain.transition.*;
import eu.domibus.connector.persistence.service.testutil.LargeFilePersistenceServicePassthroughImpl;
import eu.domibus.connector.testdata.TransitionCreator;
import eu.ecodex.dc5.message.model.*;
import org.junit.jupiter.api.*;
import org.springframework.util.StreamUtils;

import javax.activation.DataHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

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
        transformerService.messageIdThreadLocal.set(new DomibusConnectorMessageId("id1"));
    }

    @AfterEach
    public void afterEach() {
        transformerService.messageIdThreadLocal.remove();
    }


}
