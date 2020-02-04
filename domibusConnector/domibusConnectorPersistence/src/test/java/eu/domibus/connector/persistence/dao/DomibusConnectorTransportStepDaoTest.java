package eu.domibus.connector.persistence.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.github.database.rider.core.api.dataset.SeedStrategy.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@CommonPersistenceTest
@DataSet(value = "/database/testdata/dbunit/DomibusConnectorTransportStep.xml", strategy = CLEAN_INSERT)
class DomibusConnectorTransportStepDaoTest {

    @Autowired
    DomibusConnectorTransportStepDao dao;

    @Test
    void getHighestAttemptBy() {
        Optional<Integer> highestAttemptBy = dao.getHighestAttemptBy("msg1", "partner1");
        assertThat(highestAttemptBy.get()).isEqualTo(4);
    }

    @Test
    void getHighestAttemptBy_noPartner() {
        Optional<Integer> highestAttemptBy = dao.getHighestAttemptBy("msg1", "notexistant");
        assertThat(highestAttemptBy).isEmpty();
    }
}