package eu.domibus.connector.persistence.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import eu.domibus.connector.controller.service.TransportStateService;
import eu.domibus.connector.domain.enums.TransportState;
import eu.domibus.connector.domain.model.DomibusConnectorLinkPartner;
import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStep;
import eu.domibus.connector.persistence.model.PDomibusConnectorTransportStepStatusUpdate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.database.rider.core.api.dataset.SeedStrategy.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;

@CommonPersistenceTest
@DataSet(value = "/database/testdata/dbunit/DomibusConnectorTransportStep.xml", strategy = CLEAN_INSERT)
//@Disabled("Test JVM is randomly crashing on CI and via mvn")
public class DomibusConnectorTransportStepDaoTest {

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

    @Test
    public void testFindStepByLastState() {
        Pageable pageable = Pageable.ofSize(20);

        Assertions.assertAll(
                () -> assertThat(dao.findLastAttemptStepByLastStateIsOneOf(new String[]{TransportState.FAILED.getDbName()}, pageable)
                        .getTotalElements()).isEqualTo(2), //there should be 2 entries where the last updated state is failed
                () -> assertThat(dao.findLastAttemptStepByLastStateIsOneOf(new String[]{TransportState.PENDING.getDbName(), TransportState.FAILED.getDbName()}, pageable)
                        .getTotalElements()).isEqualTo(3), //there should be 3 entries where the last updated state is failed OR pending
                () -> assertThat(dao.findLastAttemptStepByLastStateIsOneOf(new String[]{TransportState.PENDING.getDbName()}, pageable)
                        .getTotalElements()).isEqualTo(1) //there should be 1 entry where the last updated state is pending
        );

    }

//    @Test
//    public void testQ() {
//        List<String> q = dao.testQ();
//        List<String> z = dao.testZ(new String[]{TransportState.FAILED.getDbName()});
//        System.out.println("####_t");
//        for (String s: q) {
//            System.out.println(s);
//        }
//        System.out.println("####_Z");
//        for (String s: z) {
//            System.out.println(s);
//        }
//    }


}