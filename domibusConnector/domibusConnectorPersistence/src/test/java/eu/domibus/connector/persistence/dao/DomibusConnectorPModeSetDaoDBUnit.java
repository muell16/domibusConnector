package eu.domibus.connector.persistence.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import eu.domibus.connector.persistence.model.PDomibusConnectorPModeSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.github.database.rider.core.api.dataset.SeedStrategy.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@CommonPersistenceTest
@DataSet(value = "/database/testdata/dbunit/DomibusConnectorPModeSet.xml", strategy = CLEAN_INSERT)
public class DomibusConnectorPModeSetDaoDBUnit {

    @Autowired
    DomibusConnectorPModeSetDao dao;

    @Test
    void getCurrentActivePModeSet() {
        List<PDomibusConnectorPModeSet> currentActivePModeSet = dao.getCurrentActivePModeSet(DomibusConnectorMessageLane.getDefaultMessageLaneId());

        assertThat(currentActivePModeSet).hasSize(1);
    }


}