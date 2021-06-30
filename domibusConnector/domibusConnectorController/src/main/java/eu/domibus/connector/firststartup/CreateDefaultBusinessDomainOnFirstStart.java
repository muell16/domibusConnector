package eu.domibus.connector.firststartup;

import eu.domibus.connector.domain.model.DomibusConnectorMessageLane;
import eu.domibus.connector.persistence.dao.DomibusConnectorMessageLaneDao;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageLane;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;

@Configuration
public class CreateDefaultBusinessDomainOnFirstStart {

    private static final Logger LOGGER = LogManager.getLogger(CreateDefaultBusinessDomainOnFirstStart.class);

    private final DomibusConnectorMessageLaneDao messageLaneDao;

    public CreateDefaultBusinessDomainOnFirstStart(DomibusConnectorMessageLaneDao messageLaneDao) {
        this.messageLaneDao = messageLaneDao;
    }

    @PostConstruct
    @Transactional
    public void createDefaultBusinessDomain() {
        Optional<PDomibusConnectorMessageLane> byName = messageLaneDao.findByName(new DomibusConnectorMessageLane.MessageLaneId(DomibusConnectorMessageLane.DEFAULT_LANE_NAME));
        if (!byName.isPresent()) {
            LOGGER.info(LoggingMarker.Log4jMarker.CONFIG, "Create default Business Message Domain [{}]", DomibusConnectorMessageLane.DEFAULT_LANE_NAME);
            PDomibusConnectorMessageLane newLane = new PDomibusConnectorMessageLane();
            newLane.setDescription("The default business message domain");
            newLane.setName(new DomibusConnectorMessageLane.MessageLaneId(DomibusConnectorMessageLane.DEFAULT_LANE_NAME));
            messageLaneDao.save(newLane);
        }
    }

}
