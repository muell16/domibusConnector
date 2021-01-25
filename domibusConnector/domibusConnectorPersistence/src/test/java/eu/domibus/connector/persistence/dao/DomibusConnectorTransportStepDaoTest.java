package eu.domibus.connector.persistence.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStep;
import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStepStatusUpdate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.database.rider.core.api.dataset.SeedStrategy.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;

@CommonPersistenceTest
@DataSet(value = "/database/testdata/dbunit/DomibusConnectorTransportStep.xml", strategy = CLEAN_INSERT)
@Disabled("Test JVM is randomly crashing on CI and via mvn")
class DomibusConnectorTransportStepDaoTest {

    @Autowired
    DomibusConnectorTransportStepDao dao;

    @Autowired
    DomibusConnectorMessageDao msgDao;

    @Test
    void getHighestAttemptBy() {
        Optional<Integer> highestAttemptBy = dao.getHighestAttemptBy("msg1", new DomibusConnectorLinkPartner.LinkPartnerName("partner1"));
        assertThat(highestAttemptBy.get()).isEqualTo(4);
    }

    @Test
    void getHighestAttemptBy_noPartner() {
        Optional<Integer> highestAttemptBy = dao.getHighestAttemptBy("msg1", new DomibusConnectorLinkPartner.LinkPartnerName("notexistant"));
        assertThat(highestAttemptBy).isEmpty();
    }

    @Test
    void testSaveRetrieve() {

        PDomibusConnectorTransportStep transportStep = new PDomibusConnectorTransportStep();
        transportStep.setAttempt(1);
        transportStep.setConnectorMessageId("msg1");
        transportStep.setLinkPartnerName(new DomibusConnectorLinkPartner.LinkPartnerName("l1"));
        transportStep.setTransportId(new TransportStateService.TransportId("msg1_1"));

        PDomibusConnectorTransportStepStatusUpdate u = new PDomibusConnectorTransportStepStatusUpdate();
        u.setCreated(LocalDateTime.now());
        u.setTransportState(TransportState.ACCEPTED);
        u.setText("text");

        transportStep.getStatusUpdates().add(u);

        PDomibusConnectorTransportStep save = dao.save(transportStep);

        Long id = save.getId();

        PDomibusConnectorTransportStep byId = dao.findById(id).get();

        byId.getStatusUpdates().forEach(s -> System.out.println(s));

    }
}