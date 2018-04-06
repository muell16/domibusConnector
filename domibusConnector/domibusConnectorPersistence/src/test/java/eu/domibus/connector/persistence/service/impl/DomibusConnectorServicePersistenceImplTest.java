package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.persistence.dao.DomibusConnectorServiceDao;
import eu.domibus.connector.persistence.model.test.util.PersistenceEntityCreator;
import eu.domibus.connector.persistence.service.DomibusConnectorServicePersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;

public class DomibusConnectorServicePersistenceImplTest {

    @Mock
    DomibusConnectorServiceDao serviceDao;

    DomibusConnectorServicePersistenceService servicePersistenceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        DomibusConnectorServicePersistenceImpl impl = new DomibusConnectorServicePersistenceImpl();
        impl.setServiceDao(serviceDao);
        servicePersistenceService = impl;
    }

    /**
     * Service
     */
    @Test
    public void testGetService() {
        Mockito.when(this.serviceDao.findOne(eq("EPO")))
                .thenReturn(PersistenceEntityCreator.createServiceEPO());

        eu.domibus.connector.domain.model.DomibusConnectorService service = servicePersistenceService.getService("EPO");

        assertThat(service).isNotNull();
        assertThat(service.getService()).isEqualTo("EPO");
        assertThat(service.getServiceType()).isEqualTo("urn:e-codex:services:");
    }


}