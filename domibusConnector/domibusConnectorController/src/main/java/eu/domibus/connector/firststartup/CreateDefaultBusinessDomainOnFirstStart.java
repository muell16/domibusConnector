package eu.domibus.connector.firststartup;

import eu.domibus.connector.common.persistence.dao.DomibusConnectorBusinessDomainDao;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.domibus.connector.persistence.model.PDomibusConnectorMessageLane;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;

@Configuration(value = CreateDefaultBusinessDomainOnFirstStart.BEAN_NAME)
public class CreateDefaultBusinessDomainOnFirstStart {

    public static final String BEAN_NAME = "CreateDefaultBusinessDomainOnFirstStartBean";
    private static final Logger LOGGER = LogManager.getLogger(CreateDefaultBusinessDomainOnFirstStart.class);

    private final DomibusConnectorBusinessDomainDao messageLaneDao;

    public CreateDefaultBusinessDomainOnFirstStart(DomibusConnectorBusinessDomainDao messageLaneDao) {
        this.messageLaneDao = messageLaneDao;
    }

    @EventListener
    @Transactional
    public void createDefaultBusinessDomain(ContextStartedEvent ctxStartEvt) {
        Optional<PDomibusConnectorMessageLane> byName = messageLaneDao.findByName(new DomibusConnectorBusinessDomain.BusinessDomainId(DomibusConnectorBusinessDomain.DEFAULT_LANE_NAME));
        if (!byName.isPresent()) {
            LOGGER.info(LoggingMarker.Log4jMarker.CONFIG, "Create default Business Message Domain [{}]", DomibusConnectorBusinessDomain.DEFAULT_LANE_NAME);
            PDomibusConnectorMessageLane newLane = new PDomibusConnectorMessageLane();
            newLane.setDescription("The default business message domain");
            newLane.setName(new DomibusConnectorBusinessDomain.BusinessDomainId(DomibusConnectorBusinessDomain.DEFAULT_LANE_NAME));
            messageLaneDao.save(newLane);
        }
    }

}
