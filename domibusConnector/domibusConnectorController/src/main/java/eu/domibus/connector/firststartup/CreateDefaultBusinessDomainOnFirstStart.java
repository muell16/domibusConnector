package eu.domibus.connector.firststartup;

import eu.ecodex.dc5.domain.repo.DomibusConnectorBusinessDomainDao;
import eu.domibus.connector.domain.model.DomibusConnectorBusinessDomain;
import eu.ecodex.dc5.domain.repo.DC5BusinessDomainJpaEntity;
import eu.domibus.connector.tools.logging.LoggingMarker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;

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

    @PostConstruct
    @Transactional
    public void createDefaultBusinessDomain() {
        Optional<DC5BusinessDomainJpaEntity> byName = messageLaneDao.findByName(new DomibusConnectorBusinessDomain.BusinessDomainId(DomibusConnectorBusinessDomain.DEFAULT_LANE_NAME));
        if (!byName.isPresent()) {
            LOGGER.info(LoggingMarker.Log4jMarker.CONFIG, "Create default Business Message Domain [{}]", DomibusConnectorBusinessDomain.DEFAULT_LANE_NAME);
            DC5BusinessDomainJpaEntity newLane = new DC5BusinessDomainJpaEntity();
            newLane.setDescription("The default business message domain");
            newLane.setName(new DomibusConnectorBusinessDomain.BusinessDomainId(DomibusConnectorBusinessDomain.DEFAULT_LANE_NAME));
            newLane.setEnabled(true);
            messageLaneDao.save(newLane);
        }
    }

}
